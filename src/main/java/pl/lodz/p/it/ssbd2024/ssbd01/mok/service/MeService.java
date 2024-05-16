package pl.lodz.p.it.ssbd2024.ssbd01.mok.service;


import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.ssbd2024.ssbd01.config.ConfigurationProperties;
import pl.lodz.p.it.ssbd2024.ssbd01.entity._enum.AccountRoleEnum;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mok.*;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.mok.*;
import pl.lodz.p.it.ssbd2024.ssbd01.mok.repository.AccountMokRepository;
import pl.lodz.p.it.ssbd2024.ssbd01.mok.repository.ChangeMyEmailRepository;
import pl.lodz.p.it.ssbd2024.ssbd01.mok.repository.ChangeMyPasswordRepository;
import pl.lodz.p.it.ssbd2024.ssbd01.mok.repository.PasswordHistoryRepository;
import pl.lodz.p.it.ssbd2024.ssbd01.util.ETagBuilder;
import pl.lodz.p.it.ssbd2024.ssbd01.util.ServiceVerifier;
import pl.lodz.p.it.ssbd2024.ssbd01.util.messages.ExceptionMessages;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MeService {

    private final AccountMokRepository accountMokRepository;

    private final PasswordEncoder passwordEncoder;

    private final PasswordHistoryRepository passwordHistoryRepository;

    private final AccountService accountService;

    private final ChangeMyPasswordRepository changeMyPasswordRepository;

    private final ChangeMyEmailRepository changeMyEmailRepository;

    private final ConfigurationProperties config;

    private final ServiceVerifier verifier;

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
    public ChangeMyPassword changeMyPasswordSendMail(String currentPassword, String newPassword)
            throws WrongOldPasswordException, ThisPasswordAlreadyWasSetInHistory {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account account = (Account) authentication.getPrincipal();
        if (!passwordEncoder.matches(currentPassword, account.getPassword())) {
            throw new WrongOldPasswordException(ExceptionMessages.WRONG_OLD_PASSWORD);
        }
        if (verifier.isPasswordInHistory(account.getId(), newPassword)) {
            throw new ThisPasswordAlreadyWasSetInHistory(ExceptionMessages.THIS_PASSWORD_ALREADY_WAS_SET_IN_HISTORY);
        }
        var randString = RandomStringUtils.random(128, 0, 0, true, true, null, new SecureRandom());
        var expiration = config.getCredentialChangeTokenExpiration();
        var expirationDate = LocalDateTime.now().plusMinutes(expiration);
        var newResetIssue = new ChangeMyPassword(randString, account,
                expirationDate, passwordEncoder.encode(newPassword));
        changeMyPasswordRepository.saveAndFlush(newResetIssue);
        return newResetIssue;
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_PARTICIPANT')")
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
    public ChangeMyEmail changeMyEmailSendMail(String currentPassword, String newEmail) throws AccountNotFoundException, WrongOldPasswordException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account account = (Account) authentication.getPrincipal();
        if (!passwordEncoder.matches(currentPassword, account.getPassword())) {
            throw new WrongOldPasswordException(ExceptionMessages.WRONG_OLD_PASSWORD);
        }
        var randString = RandomStringUtils.random(128, 0, 0, true, true, null, new SecureRandom());
        var expiration = config.getCredentialChangeTokenExpiration();
        var expirationDate = LocalDateTime.now().plusMinutes(expiration);
        var newResetIssue = new ChangeMyEmail(randString, account,
                expirationDate, newEmail);
        changeMyEmailRepository.saveAndFlush(newResetIssue);
        return newResetIssue;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
    public void changeMyPasswordWithToken(String token)
            throws TokenExpiredException, AccountNotFoundException, TokenNotFoundException {
        Account account = verifier.verifyCredentialReset(token, changeMyPasswordRepository);
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
        Account accountToUpdate = verifier.verifyCredentialReset(token, changeMyEmailRepository);
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

}
