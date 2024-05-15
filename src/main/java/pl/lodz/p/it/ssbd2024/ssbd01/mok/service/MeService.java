package pl.lodz.p.it.ssbd2024.ssbd01.mok.service;


import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.core.env.Environment;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
import pl.lodz.p.it.ssbd2024.ssbd01.mok.repository.AccountMokRepository;
import pl.lodz.p.it.ssbd2024.ssbd01.mok.repository.ChangeMyEmailRepository;
import pl.lodz.p.it.ssbd2024.ssbd01.mok.repository.ChangeMyPasswordRepository;
import pl.lodz.p.it.ssbd2024.ssbd01.mok.repository.PasswordHistoryRepository;
import pl.lodz.p.it.ssbd2024.ssbd01.util.AbstractCredentialChange;
import pl.lodz.p.it.ssbd2024.ssbd01.util.ETagBuilder;
import pl.lodz.p.it.ssbd2024.ssbd01.util.MailService;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class MeService {

    private final AccountMokRepository accountMokRepository;

    private final PasswordEncoder passwordEncoder;

    private final PasswordHistoryRepository passwordHistoryRepository;

    private final AccountService accountService;

    private final ChangeMyPasswordRepository changeMyPasswordRepository;

    private final ChangeMyEmailRepository changeMyEmailRepository;

    private final MailService mailService;

    private final Environment env;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_PARTICIPANT')")
    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
    public Account getAccount() throws AccountNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account account = (Account) authentication.getPrincipal();
        return accountMokRepository.findById(account.getId())
                .orElseThrow(() -> new AccountNotFoundException(ExceptionMessages.ACCOUNT_NOT_FOUND));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_PARTICIPANT')")
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
    public String changeMyPasswordSendMail(String currentPassword, String newPassword) throws AccountNotFoundException, WrongOldPasswordException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account account = (Account) authentication.getPrincipal();

        ThrowingSupplier<Exception> passwordHistoryCheck = () -> {
            if (accountService.isPasswordInHistory(account.getId(), newPassword)) {
                throw new ThisPasswordAlreadyWasSetInHistory(ExceptionMessages.THIS_PASSWORD_ALREADY_WAS_SET_IN_HISTORY);
            }
            return null;
        };

        return getNewResetIssue(
                account.getId(),
                currentPassword,
                passwordEncoder.encode(newPassword),
                passwordHistoryCheck,
                new MailResetIssuePropertiesDTO(
                        "change-my-password",
                        "mail.password.changed.by.you.subject",
                        "mail.password.changed.by.you.body"
                ),
                this::getChangeMyPassword
        );
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_PARTICIPANT')")
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
    public String changeMyEmailSendMail(String currentPassword, String newEmail) throws AccountNotFoundException, WrongOldPasswordException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account account = (Account) authentication.getPrincipal();
        return getNewResetIssue(
                account.getId(),
                currentPassword,
                newEmail,
                null,
                new MailResetIssuePropertiesDTO(
                        "change-my-email",
                        "mail.email.changed.by.you.subject",
                        "mail.email.changed.by.you.body"
                ),
                this::getChangeMyEmail
        );
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
    public void changeMyPasswordWithToken(String token)
            throws TokenExpiredException, AccountNotFoundException, TokenNotFoundException {
        Account account = accountService.verifyCredentialReset(token, changeMyPasswordRepository);
        String password =
                changeMyPasswordRepository.findByToken(token).orElseThrow(() -> new TokenNotFoundException(ExceptionMessages.TOKEN_NOT_FOUND))
                        .getPassword();
        account.setPassword(password);
        passwordHistoryRepository.saveAndFlush(new PasswordHistory(account));
        accountMokRepository.saveAndFlush(account);
        changeMyPasswordRepository.deleteByToken(token);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
    public void changeMyEmailWithToken(String token)
            throws AccountNotFoundException, TokenNotFoundException, TokenExpiredException {

        Account accountToUpdate = accountService.verifyCredentialReset(token, changeMyEmailRepository);
        var changeMyEmail = changeMyEmailRepository.findByToken(token)
                .orElseThrow(() -> new TokenNotFoundException(ExceptionMessages.EMAIL_RESET_TOKEN_NOT_FOUND));
        accountToUpdate.setEmail(changeMyEmail.getEmail());
        accountMokRepository.saveAndFlush(accountToUpdate);
        changeMyEmailRepository.deleteByToken(token);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_PARTICIPANT')")
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
    public Account updateMyAccountData(Account accountData, String eTag) throws AccountNotFoundException, OptLockException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account account = (Account) authentication.getPrincipal();
        Account accountToUpdate = accountMokRepository.findById(account.getId())
                .orElseThrow(() -> new AccountNotFoundException(ExceptionMessages.ACCOUNT_NOT_FOUND));
        if (!ETagBuilder.isETagValid(eTag, String.valueOf(accountToUpdate.getVersion()))) {
            throw new OptLockException(ExceptionMessages.OPTIMISTIC_LOCK_EXCEPTION);
        }
        accountToUpdate.setFirstName(accountData.getFirstName());
        accountToUpdate.setLastName(accountData.getLastName());
        accountToUpdate.setGender(accountData.getGender());
        return accountMokRepository.saveAndFlush(accountToUpdate);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_PARTICIPANT')")
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
    public void logSwitchRole(AccountRoleEnum roleEnum) throws AccountNotFoundException, RoleNotAssignedToAccount {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account account = (Account) authentication.getPrincipal();
        Account accountToLog = accountMokRepository.findById(account.getId())
                .orElseThrow(() -> new AccountNotFoundException(ExceptionMessages.ACCOUNT_NOT_FOUND));
        List<Role> accountRoles = accountToLog.getRoles();
        if (!accountRoles.contains(new Role(roleEnum))) {
            throw new RoleNotAssignedToAccount(ExceptionMessages.ACCOUNT_NOT_HAVE_THIS_ROLE);
        }
    }

    @Transactional(propagation = Propagation.MANDATORY, rollbackFor = {Exception.class})
    protected String getNewResetIssue(
            UUID id, String currentPassword,
            String newCredential,
            ThrowingSupplier<Exception> additionalCondition,
            MailResetIssuePropertiesDTO mailResetIssuePropertiesDTO,
            Function<CredentialUpdateArgumentDTO, AbstractCredentialChange> tokenFunction
    ) throws WrongOldPasswordException, AccountNotFoundException {
        Account account = accountMokRepository.findById(id).orElseThrow(() -> new AccountNotFoundException(ExceptionMessages.ACCOUNT_NOT_FOUND));
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

    public void sendMailForMyCredentialReset(String endpointName, String mailSubject, String mailBody, AbstractCredentialChange newResetIssue) {
        StringBuilder sb = new StringBuilder();
        sb.append("<a href='https://team-1.proj-sum.it.p.lodz.pl/");
        sb.append(endpointName);
        sb.append("?token=");
        sb.append(newResetIssue.getToken());
        sb.append("'>Link</a>");
        mailService.sendEmail(newResetIssue.getAccount(), mailSubject,
                mailBody, new Object[] {sb});
    }

    @Transactional(propagation = Propagation.MANDATORY, rollbackFor = {Exception.class})
    public AbstractCredentialChange getChangeMyPassword(CredentialUpdateArgumentDTO credentialUpdateArgumentDTO) {
        var randString = RandomStringUtils.random(128, 0, 0, true, true, null, new SecureRandom());
        var expiration = Integer.parseInt(Objects.requireNonNull(env.getProperty("credential_change.token.expiration.minutes")));
        var expirationDate = LocalDateTime.now().plusMinutes(expiration);
        var newResetIssue = new ChangeMyPassword(randString, credentialUpdateArgumentDTO.account(),
                expirationDate, credentialUpdateArgumentDTO.newCredential());
        changeMyPasswordRepository.saveAndFlush(newResetIssue);
        return newResetIssue;
    }

    @Transactional(propagation = Propagation.MANDATORY, rollbackFor = {Exception.class})
    public AbstractCredentialChange getChangeMyEmail(CredentialUpdateArgumentDTO credentialUpdateArgumentDTO) {
        var randString = RandomStringUtils.random(128, 0, 0, true, true, null, new SecureRandom());
        var expiration = Integer.parseInt(Objects.requireNonNull(env.getProperty("credential_change.token.expiration.minutes")));
        var expirationDate = LocalDateTime.now().plusMinutes(expiration);
        var newResetIssue = new ChangeMyEmail(randString, credentialUpdateArgumentDTO.account(),
                expirationDate, credentialUpdateArgumentDTO.newCredential());
        return changeMyEmailRepository.saveAndFlush(newResetIssue);
    }
}
