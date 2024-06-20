package pl.lodz.p.it.ssbd2024.ssbd01.util.mail;

import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mok.ChangeEmail;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mok.ChangeMyPassword;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mok.CredentialReset;
import pl.lodz.p.it.ssbd2024.ssbd01.util._enum.AccountRoleEnum;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@AllArgsConstructor
@Transactional(propagation = Propagation.MANDATORY)
@PreAuthorize("hasRole('ROLE_SYSTEM')")
public class MailService {

    private final MailTemplateService mailTemplateService;


    public void sendEmailToAddRoleToAccount(Account mailTo, String roleName) {
        mailTemplateService.sendEmailTemplate(mailTo, "mail.role.added.subject", "mail.role.added.body", new Object[] {roleName});
    }

    public void sendEmailToRemoveRoleFromAccount(Account mailTo, String roleName) {
        mailTemplateService.sendEmailTemplate(mailTo, "mail.role.removed.subject", "mail.role.removed.body", new Object[] {roleName});
    }

    public void sendEmailToSetActiveAccount(Account mailTo) {
        mailTemplateService.sendEmailTemplate(mailTo, "mail.unblocked.subject", "mail.unblocked.body", null);
    }

    public void sendEmailToSetInactiveAccount(Account mailTo) {
        mailTemplateService.sendEmailTemplate(mailTo, "mail.blocked.subject", "mail.blocked.body", null);
    }

    public void sendEmailToResetPassword(CredentialReset credentialReset) {
        StringBuilder sb = new StringBuilder();
        sb.append("<a href='https://team-1.proj-sum.it.p.lodz.pl/login/reset-password?token=");
        sb.append(credentialReset.getToken());
        sb.append("'>Link</a>");
        mailTemplateService.sendEmailTemplate(credentialReset.getAccount(), "mail.password.reset.subject",
                "mail.password.reset.body", new Object[] {sb});
    }

    public void sendEmailToChangePasswordByAdmin(CredentialReset credentialReset) {
        StringBuilder sb = new StringBuilder();
        sb.append("<a href='https://team-1.proj-sum.it.p.lodz.pl/login/reset-password?token=");
        sb.append(credentialReset.getToken());
        sb.append("'>Link</a>");
        mailTemplateService.sendEmailTemplate(credentialReset.getAccount(), "mail.password.changed.by.admin.subject",
                "mail.password.changed.by.admin.body", new Object[] {sb});
    }

    public void sendEmailToChangeEmailByAdmin(ChangeEmail changeEmail, String email) {
        StringBuilder sb = new StringBuilder();
        sb.append("<a href='https://team-1.proj-sum.it.p.lodz.pl/confirm-email?token=");
        sb.append(changeEmail.getToken());
        sb.append("'>Link</a>");
        mailTemplateService.sendEmailOnNewMail(changeEmail.getAccount(), "mail.email.changed.by.admin.subject",
                "mail.email.changed.by.admin.body", new Object[] {sb}, email);
    }

    public void sendEmailToChangeMyPassword(ChangeMyPassword newResetIssue) {
        StringBuilder sb = new StringBuilder();
        sb.append("<a href='https://team-1.proj-sum.it.p.lodz.pl/");
        sb.append("change-my-password");
        sb.append("?token=");
        sb.append(newResetIssue.getToken());
        sb.append("'>Link</a>");
        mailTemplateService.sendEmailTemplate(newResetIssue.getAccount(), "mail.password.changed.by.you.subject",
                "mail.password.changed.by.you.body", new Object[] {sb});
    }

    public void sendEmailToChangeMyEmail(ChangeEmail changeEmail, String email) {
        StringBuilder sb = new StringBuilder();
        sb.append("<a href='https://team-1.proj-sum.it.p.lodz.pl/");
        sb.append("confirm-email");
        sb.append("?token=");
        sb.append(changeEmail.getToken());
        sb.append("'>Link</a>");
        mailTemplateService.sendEmailOnNewMail(changeEmail.getAccount(), "mail.email.changed.by.you.subject",
                "mail.email.changed.by.you.body", new Object[] {sb}, email);
    }

    public void sendEmailToVerifyAccount(Account account, String randString) {
        StringBuilder sb = new StringBuilder();
        sb.append("<a href='https://team-1.proj-sum.it.p.lodz.pl/verify-account?token=");
        sb.append(randString);
        sb.append("'>Link</a>");
        mailTemplateService.sendEmailTemplate(account, "mail.verify.account.subject",
                "mail.verify.account.body", new Object[] {sb});
    }

    public void sendEmailToInformAboutVerification(Account account) {
        mailTemplateService.sendEmailTemplate(account, "mail.after.verify.subject", "mail.after.verify.body",
                new Object[] {AccountRoleEnum.ROLE_PARTICIPANT});
    }

    public void sendEmailToInformAboutUnblockAccount(Account account) {
        mailTemplateService.sendEmailTemplate(account, "mail.unblocked.subject", "mail.unblocked.body", null);
    }

    public void sendEmailToUnblockAccountViaLink(Account account, String token) {
        StringBuilder sb = new StringBuilder();
        sb.append("<a href='https://team-1.proj-sum.it.p.lodz.pl/unblock-account?token=");
        sb.append(token);
        sb.append("'>Link</a>");
        mailTemplateService.sendEmailTemplate(account, "mail.unblock.account.subject", "mail.unblock.account.body", new Object[] {sb});
    }

    public void sendEmailAfterFailedLoginAttempts(Account account, LocalDateTime lockTimeout) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        mailTemplateService.sendEmailTemplate(account, "mail.locked.until.subject", "mail.locked.until.body",
                new Object[] {lockTimeout.format(formatter)});
    }

    public void sendEmailOnDeleteUnverifiedAccount(Account account) {
        mailTemplateService.sendEmailTemplate(account, "mail.delete.account.subject", "mail.delete.account.body", null);
    }

    public void sendEmailAdminNewLogin(Account account, String ipAddress) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime lockTimeout = LocalDateTime.now();
        mailTemplateService.sendEmailTemplate(account, "mail.admin.login.subject", "mail.admin.login.body",
                new Object[] {ipAddress, lockTimeout.format(formatter)});
    }

    public void sendEmailConfirmationReminder(Account account, String token) {
        StringBuilder sb = new StringBuilder();
        sb.append("<a href='https://team-1.proj-sum.it.p.lodz.pl/verify-account?token=");
        sb.append(token);
        sb.append("'>Link</a>");
        mailTemplateService.sendEmailTemplate(account, "mail.verify.account.subject",
                "mail.verify.account.body", new Object[] {sb});
    }

    public void sendEmailOnEventCancel(Account account, String eventName) {
        mailTemplateService.sendEmailTemplate(account, "mail.event.cancel.subject", "mail.event.cancel.body", new Object[] {eventName});
    }

    public void sendEmailOnSessionCancel(Account account, String sessionName) {
        mailTemplateService.sendEmailTemplate(account, "mail.session.cancel.subject", "mail.session.cancel.body", new Object[] {sessionName});
    }
}
