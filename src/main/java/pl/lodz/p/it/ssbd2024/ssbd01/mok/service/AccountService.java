package pl.lodz.p.it.ssbd2024.ssbd01.mok.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.function.ThrowingSupplier;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.CredentialUpdateArgumentDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.MailResetIssuePropertiesDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.entity._enum.AccountRoleEnum;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mok.*;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.mok.*;
import pl.lodz.p.it.ssbd2024.ssbd01.messages.ExceptionMessages;
import pl.lodz.p.it.ssbd2024.ssbd01.mok.repository.*;
import pl.lodz.p.it.ssbd2024.ssbd01.util.AbstractCredentialChange;
import pl.lodz.p.it.ssbd2024.ssbd01.util.ETagBuilder;
import pl.lodz.p.it.ssbd2024.ssbd01.util.MailService;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;


@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountMokRepository accountMokRepository;
    private final RoleRepository roleRepository;
    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;
    private final Environment env;
    private final PasswordHistoryRepository passwordHistoryRepository;
    private final ChangeMyPasswordRepository changeMyPasswordRepository;
    private final ChangeMyEmailRepository changeMyEmailRepository;
    private final CredentialResetRepository resetMyCredentialRepository;


    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
    public List<Account> getAllAccounts() {
        return accountMokRepository.findAll();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
    public Account addAccount(Account account) {
        Account returnedAccount = accountMokRepository.saveAndFlush(account);
        passwordHistoryRepository.saveAndFlush(new PasswordHistory(returnedAccount));
        return returnedAccount;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
    public Account addRoleToAccount(UUID id, AccountRoleEnum roleName)
            throws RoleAlreadyAssignedException, WrongRoleToAccountException, RoleNotFoundException,
            AccountNotFoundException {
        Role role = roleRepository.findByName(roleName).orElseThrow(() -> new RoleNotFoundException(ExceptionMessages.ROLE_NOT_FOUND));
        Account account = accountMokRepository.findById(id).orElseThrow(() -> new AccountNotFoundException(ExceptionMessages.ACCOUNT_NOT_FOUND));
        List<Role> accountRoles = account.getRoles();
        if (accountRoles.contains(new Role(roleName))) {
            throw new RoleAlreadyAssignedException(ExceptionMessages.ROLE_ALREADY_ASSIGNED);
        }
        switch (roleName) {
            case PARTICIPANT:
                canAddParticipantRole(account);
                break;
            case MANAGER, ADMIN:
                canAddManagerOrAdminRole(account);
                break;
            default:
                throw new RoleNotFoundException(ExceptionMessages.ROLE_NOT_FOUND);
        }
        account.addRole(role);
        mailService.sendEmail(account, "mail.role.added.subject", "mail.role.added.body", new Object[] {roleName.name()});
        return accountMokRepository.saveAndFlush(account);
    }

    private void canAddManagerOrAdminRole(Account account) throws WrongRoleToAccountException {
        List<Role> accountRoles = account.getRoles();
        if (accountRoles.contains(new Role(AccountRoleEnum.PARTICIPANT))) {
            throw new WrongRoleToAccountException(ExceptionMessages.PARTICIPANT_CANNOT_HAVE_OTHER_ROLES);
        }
    }

    private void canAddParticipantRole(Account account) throws WrongRoleToAccountException {
        List<Role> accountRoles = account.getRoles();
        if (!accountRoles.isEmpty()) {
            throw new WrongRoleToAccountException(ExceptionMessages.PARTICIPANT_CANNOT_HAVE_OTHER_ROLES);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
    public Account removeRole(UUID id, AccountRoleEnum roleName) throws RoleNotFoundException, AccountNotFoundException, RoleCanNotBeRemoved {
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RoleNotFoundException(ExceptionMessages.ROLE_NOT_FOUND));
        Account account = accountMokRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(ExceptionMessages.ACCOUNT_NOT_FOUND));
        for (Role roles : account.getRoles()) {
            if (roles.getName().equals(roleName)) {
                account.removeRole(role);
                mailService.sendEmail(account, "mail.role.removed.subject", "mail.role.removed.body", new Object[] {roleName.name()});
                return accountMokRepository.saveAndFlush(account);
            }
        }
        throw new RoleCanNotBeRemoved(ExceptionMessages.ACCOUNT_NOT_HAVE_THIS_ROLE);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
    public Account setAccountStatus(UUID id, boolean status) throws AccountNotFoundException {
        Account account = accountMokRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(ExceptionMessages.ACCOUNT_NOT_FOUND));
        account.setActive(status);
        if (status) {
            mailService.sendEmail(account, "mail.unblocked.subject", "mail.unblocked.body", new Object[] {});
        } else {
            mailService.sendEmail(account, "mail.blocked.subject", "mail.blocked.body", new Object[] {});
        }
        return accountMokRepository.saveAndFlush(account);
    }


    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
    public Account getAccountByUsername(String username) throws AccountNotFoundException {
        return accountMokRepository.findByUsername(username)
                .orElseThrow(() -> new AccountNotFoundException(ExceptionMessages.ACCOUNT_NOT_FOUND));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
    public Account updateAccountData(UUID id, Account account, String eTag) throws AccountNotFoundException, OptLockException {
        Account accountToUpdate = accountMokRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(ExceptionMessages.ACCOUNT_NOT_FOUND));
        if (!ETagBuilder.isETagValid(eTag, String.valueOf(accountToUpdate.getVersion()))) {
            throw new OptLockException(ExceptionMessages.OPTIMISTIC_LOCK_EXCEPTION);
        }
        accountToUpdate.setFirstName(account.getFirstName());
        accountToUpdate.setLastName(account.getLastName());
        accountToUpdate.setGender(account.getGender());
        return accountMokRepository.saveAndFlush(accountToUpdate);
    }


    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
    public List<Account> getParticipants() throws RoleNotFoundException {
        Role role =
                roleRepository.findByName(AccountRoleEnum.PARTICIPANT).orElseThrow(() -> new RoleNotFoundException(ExceptionMessages.ROLE_NOT_FOUND));
        return accountMokRepository.findAccountByRolesContains(role);
    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
    public List<Account> getManagers() throws RoleNotFoundException {
        Role role = roleRepository.findByName(AccountRoleEnum.MANAGER).orElseThrow(() -> new RoleNotFoundException(ExceptionMessages.ROLE_NOT_FOUND));
        return accountMokRepository.findAccountByRolesContains(role);
    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
    public List<Account> getAdmins() throws RoleNotFoundException {
        Role role = roleRepository.findByName(AccountRoleEnum.ADMIN).orElseThrow(() -> new RoleNotFoundException(ExceptionMessages.ROLE_NOT_FOUND));
        return accountMokRepository.findAccountByRolesContains(role);
    }


    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
    public Account getAccountById(UUID id) throws AccountNotFoundException {
        return accountMokRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(ExceptionMessages.ACCOUNT_NOT_FOUND));
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
    public void resetPasswordAndSendEmail(String email) {
        Optional<Account> account = accountMokRepository.findByEmail(email);
        if (account.isEmpty()) {
            return;
        }
        CredentialReset credentialReset = saveTokenToChangeCredential(account.get());
        StringBuilder sb = new StringBuilder();
        sb.append("<a href='https://team-1.proj-sum.it.p.lodz.pl/login/reset-password?token=");
        sb.append(credentialReset.getToken());
        sb.append("'>Link</a>");
        mailService.sendEmail(credentialReset.getAccount(), "mail.password.reset.subject",
                "mail.password.reset.body", new Object[] {sb});
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
    public String changePasswordAndSendEmail(String email) throws AccountNotFoundException {
        Account account = accountMokRepository.findByEmail(email)
                .orElseThrow(() -> new AccountNotFoundException(ExceptionMessages.ACCOUNT_NOT_FOUND));
        CredentialReset credentialReset = saveTokenToChangeCredential(account);
        StringBuilder sb = new StringBuilder();
        sb.append("<a href='https://team-1.proj-sum.it.p.lodz.pl/login/reset-password?token=");
        sb.append(credentialReset.getToken());
        sb.append("'>Link</a>");
        mailService.sendEmail(credentialReset.getAccount(), "mail.password.changed.by.admin.subject",
                "mail.password.changed.by.admin.body", new Object[] {sb});
        return credentialReset.getToken();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
    public String sendMailWhenEmailChange(UUID id, String email) throws AccountNotFoundException {
        Account account = accountMokRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(ExceptionMessages.ACCOUNT_NOT_FOUND));
        CredentialReset credentialReset = saveTokenToChangeCredential(account);
        StringBuilder sb = new StringBuilder();
        sb.append("<a href='https://team-1.proj-sum.it.p.lodz.pl/login/change-email?token=");
        sb.append(credentialReset.getToken());
        sb.append("'>Link</a>");
        mailService.sendEmailOnNewMail(credentialReset.getAccount(), "mail.email.changed.by.admin.subject",
                "mail.email.changed.by.admin.body", new Object[] {sb}, email);
        return credentialReset.getToken();

    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
    public void resetPasswordWithToken(String token, String newPassword)
            throws AccountNotFoundException, TokenExpiredException, ThisPasswordAlreadyWasSetInHistory, TokenNotFoundException {
        Account accountToUpdate = verifyCredentialReset(token,resetMyCredentialRepository);
        if (isPasswordInHistory(accountToUpdate.getId(), newPassword)) {
            throw new ThisPasswordAlreadyWasSetInHistory(ExceptionMessages.THIS_PASSWORD_ALREADY_WAS_SET_IN_HISTORY);
        }
        accountToUpdate.setPassword(passwordEncoder.encode(newPassword));
        passwordHistoryRepository.saveAndFlush(new PasswordHistory(accountToUpdate));
        accountMokRepository.saveAndFlush(accountToUpdate);
        resetMyCredentialRepository.deleteByToken(token);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
    public void changeEmailWithToken(String token, String newEmail)
            throws TokenExpiredException, AccountNotFoundException, EmailAlreadyExistsException, TokenNotFoundException {
        if (accountMokRepository.findByEmail(newEmail).isPresent()) {
            throw new EmailAlreadyExistsException(ExceptionMessages.EMAIL_ALREADY_EXISTS);
        }
        Account accountToUpdate = verifyCredentialReset(token,resetMyCredentialRepository);
        accountToUpdate.setEmail(newEmail);
        accountMokRepository.saveAndFlush(accountToUpdate);
        resetMyCredentialRepository.deleteByToken(token);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
    public void changeMyEmailWithToken(String token)
            throws AccountNotFoundException, TokenNotFoundException, TokenExpiredException {

        Account accountToUpdate = verifyCredentialReset(token,changeMyEmailRepository);
        var changeMyEmail = changeMyEmailRepository.findByToken(token)
                .orElseThrow(() -> new TokenNotFoundException(ExceptionMessages.EMAIL_RESET_TOKEN_NOT_FOUND));
        accountToUpdate.setEmail(changeMyEmail.getEmail());
        accountMokRepository.saveAndFlush(accountToUpdate);
        changeMyEmailRepository.deleteByToken(token);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
    public String changeMyPasswordSendMail(UUID id, String currentPassword, String newPassword)
            throws AccountNotFoundException, WrongOldPasswordException {
        ThrowingSupplier<Exception> passwordHistoryCheck = () -> {
            if (isPasswordInHistory(id, newPassword)) {
                throw new ThisPasswordAlreadyWasSetInHistory(ExceptionMessages.THIS_PASSWORD_ALREADY_WAS_SET_IN_HISTORY);
            }
            return null;
        };
        return getNewResetIssue(id,
                currentPassword,
                passwordEncoder.encode(newPassword),
                passwordHistoryCheck,
                new MailResetIssuePropertiesDTO("change-my-password",
                        "mail.password.changed.by.you.subject",
                        "mail.password.changed.by.you.body"),
                this::getChangeMyPassword);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
    public String changeMyEmailSendMail(UUID id, String currentPassword, String newEmail)
            throws AccountNotFoundException, WrongOldPasswordException {
        return getNewResetIssue(id,
                currentPassword,
                newEmail,
                null,
                new MailResetIssuePropertiesDTO("change-my-email",
                "mail.email.changed.by.you.subject",
                "mail.email.changed.by.you.body"),
                this::getChangeMyEmail);
    }

    private String getNewResetIssue(UUID id,
                                    String currentPassword,
                                    String newCredential,
                                    ThrowingSupplier<Exception> additionalCondition,
                                    MailResetIssuePropertiesDTO mailResetIssuePropertiesDTO,
                                    Function<CredentialUpdateArgumentDTO, AbstractCredentialChange> tokenFunction)
            throws WrongOldPasswordException, AccountNotFoundException {
        Account account = accountMokRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(ExceptionMessages.ACCOUNT_NOT_FOUND));
        if (!passwordEncoder.matches(currentPassword, account.getPassword())) {
            throw new WrongOldPasswordException(ExceptionMessages.WRONG_OLD_PASSWORD);
        }
        if (additionalCondition != null) {
            additionalCondition.get();
        }
        var newResetIssue = tokenFunction.apply(new CredentialUpdateArgumentDTO(newCredential, account));
        sendMailForMyCredentialReset(mailResetIssuePropertiesDTO.endpointName(),
                mailResetIssuePropertiesDTO.mailSubject(),
                mailResetIssuePropertiesDTO.mailBody(),
                newResetIssue);
        return newResetIssue.getToken();
    }

    private void sendMailForMyCredentialReset(String endpointName, String mailSubject, String mailBody, AbstractCredentialChange newResetIssue) {
        StringBuilder sb = new StringBuilder();
        sb.append("<a href='https://team-1.proj-sum.it.p.lodz.pl/");
        sb.append(endpointName);
        sb.append("?token=");
        sb.append(newResetIssue.getToken());
        sb.append("'>Link</a>");
        mailService.sendEmail(newResetIssue.getAccount(), mailSubject,
                mailBody, new Object[] {sb});
    }

    private AbstractCredentialChange getChangeMyPassword(CredentialUpdateArgumentDTO credentialUpdateArgumentDTO) {
        var randString = RandomStringUtils.random(128, 0, 0, true, true, null, new SecureRandom());
        var expiration = Integer.parseInt(Objects.requireNonNull(env.getProperty("credential_change.token.expiration.minutes")));
        var expirationDate = LocalDateTime.now().plusMinutes(expiration);
        var newResetIssue = new ChangeMyPassword(randString, credentialUpdateArgumentDTO.account(),
                expirationDate, credentialUpdateArgumentDTO.newCredential());
        changeMyPasswordRepository.saveAndFlush(newResetIssue);
        return newResetIssue;
    }

    private AbstractCredentialChange getChangeMyEmail(CredentialUpdateArgumentDTO credentialUpdateArgumentDTO) {
        var randString = RandomStringUtils.random(128, 0, 0, true, true, null, new SecureRandom());
        var expiration = Integer.parseInt(Objects.requireNonNull(env.getProperty("credential_change.token.expiration.minutes")));
        var expirationDate = LocalDateTime.now().plusMinutes(expiration);
        var newResetIssue = new ChangeMyEmail(randString, credentialUpdateArgumentDTO.account(),
                expirationDate, credentialUpdateArgumentDTO.newCredential());
        return changeMyEmailRepository.saveAndFlush(newResetIssue);
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
    public void changeMyPasswordWithToken(String token)
            throws TokenExpiredException, AccountNotFoundException, TokenNotFoundException {
        Account account = verifyCredentialReset(token, changeMyPasswordRepository);
        String password = changeMyPasswordRepository.findByToken(token).get().getPassword();
        account.setPassword(password);
        passwordHistoryRepository.saveAndFlush(new PasswordHistory(account));
        accountMokRepository.saveAndFlush(account);
        changeMyPasswordRepository.deleteByToken(token);

    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
    public <T extends AbstractCredentialChange> Account verifyCredentialReset(String token, GenericChangeCredentialTokenRepository<T> repo)
            throws AccountNotFoundException, TokenExpiredException, TokenNotFoundException {
        Optional<T> credentialReset = repo.findByToken(token);
        if (credentialReset.isEmpty()) {
            throw new TokenNotFoundException(ExceptionMessages.TOKEN_NOT_FOUND);
        }
        if (credentialReset.get().getExpirationDate().isBefore(LocalDateTime.now())) {
            throw new TokenExpiredException(ExceptionMessages.TOKEN_EXPIRED);
        }
        return accountMokRepository.findByEmail(credentialReset.get().getAccount().getEmail())
                .orElseThrow(() -> new AccountNotFoundException(ExceptionMessages.ACCOUNT_NOT_FOUND));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
    public void logSwitchRole(UUID accountId, AccountRoleEnum roleEnum) throws AccountNotFoundException, RoleNotAssignedToAccount {
        Account account = accountMokRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(ExceptionMessages.ACCOUNT_NOT_FOUND));
        List<Role> accountRoles = account.getRoles();
        if (!accountRoles.contains(new Role(roleEnum))) {
            throw new RoleNotAssignedToAccount(ExceptionMessages.ACCOUNT_NOT_HAVE_THIS_ROLE);
        }
    }

    private boolean isPasswordInHistory(UUID accountId, String password) {
        return passwordHistoryRepository.findPasswordHistoryByAccount_Id(accountId)
                .stream().anyMatch(passwordHistory -> passwordEncoder.matches(password, passwordHistory.getPassword()));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
    public CredentialReset saveTokenToChangeCredential(Account account) {
        var randString = RandomStringUtils.random(128, 0, 0, true, true, null, new SecureRandom());
        var expiration = Integer.parseInt(Objects.requireNonNull(env.getProperty("credential_change.token.expiration.minutes")));
        var expirationDate = LocalDateTime.now().plusMinutes(expiration);
        var newResetIssue = new CredentialReset(randString, account, expirationDate);
        resetMyCredentialRepository.saveAndFlush(newResetIssue);
        return newResetIssue;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
    @Scheduled(fixedRate = 120000)
    public void deleteExpiredTokens() {
        resetMyCredentialRepository.deleteAllByExpirationDateBefore(LocalDateTime.now());
        changeMyPasswordRepository.deleteAllByExpirationDateBefore(LocalDateTime.now());
        changeMyEmailRepository.deleteAllByExpirationDateBefore(LocalDateTime.now());
    }


}


