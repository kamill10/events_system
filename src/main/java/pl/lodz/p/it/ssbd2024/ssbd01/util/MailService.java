package pl.lodz.p.it.ssbd2024.ssbd01.util;

import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import pl.lodz.p.it.ssbd2024.ssbd01.config.ConfigurationProperties;
import pl.lodz.p.it.ssbd2024.ssbd01.entity._enum.AccountRoleEnum;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.logging.Logger;

@Service
@AllArgsConstructor
public class MailService {

    private JavaMailSender mailSender;
    private MessageSource messageSource;
    private final ConfigurationProperties config;

    private void sendEmail(Mail mail) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            mimeMessageHelper.setSubject(mail.getMailSubject());
            mimeMessageHelper.setFrom(new InternetAddress(mail.getMailFrom()));
            mimeMessageHelper.setTo(mail.getMailTo());
            mimeMessageHelper.setText(mail.getMailContent(), true);
            mailSender.send(mimeMessageHelper.getMimeMessage());
        } catch (Exception e) {
            Logger.getGlobal().severe(e.getMessage());
        }
    }

    public void sendEmailTemplate(Account mailTo, String mailSubject, String mailContent, Object[] contentArgs) {
        Locale locale = Locale.forLanguageTag(mailTo.getLanguage().getLanguageCode());
        String subject = messageSource.getMessage(mailSubject, null, locale);
        String mailBody = messageSource.getMessage(mailContent, contentArgs, locale);
        String name = messageSource.getMessage("mail.hello", new Object[] {mailTo.getFirstName()}, locale);
        String mailText = "<html> <body> <h2> " + name + "</h2>" + "<p> " + mailBody + " </p> </body> </html>";
        Mail mail = new Mail(mailTo.getEmail(), subject, mailText);

        sendEmail(mail);
    }

    public void sendEmailOnNewMail(Account mailTo, String mailSubject, String mailContent, Object[] contentArgs, String newEmail) {
        Locale locale = Locale.forLanguageTag(mailTo.getLanguage().getLanguageCode());
        String subject = messageSource.getMessage(mailSubject, null, locale);
        String mailBody = messageSource.getMessage(mailContent, contentArgs, locale);
        String name = messageSource.getMessage("mail.hello", new Object[] {mailTo.getFirstName()}, locale);
        String mailText = "<html> <body> <h2> " + name + "</h2>" + "<p> " + mailBody + " </p> </body> </html>";
        Mail mail = new Mail(newEmail, subject, mailText);

        sendEmail(mail);
    }

    public void sendEmailToAddRoleToAccount(Account mailTo, String roleName) {
        sendEmailTemplate(mailTo, "mail.role.added.subject", "mail.role.added.body", new Object[] {roleName});
    }

    public void sendEmailToRemoveRoleFromAccount(Account mailTo, String roleName) {
        sendEmailTemplate(mailTo, "mail.role.removed.subject", "mail.role.removed.body", new Object[] {roleName});
    }

    public void sendEmailToSetActiveAccount(Account mailTo) {
        sendEmailTemplate(mailTo, "mail.unblocked.subject", "mail.unblocked.body", null);
    }

    public void sendEmailToSetInactiveAccount(Account mailTo) {
        sendEmailTemplate(mailTo, "mail.blocked.subject", "mail.blocked.body", null);
    }

    public void sendEmailToResetPassword(CredentialReset credentialReset) {
        StringBuilder sb = new StringBuilder();
        sb.append("<a href='https://team-1.proj-sum.it.p.lodz.pl/login/reset-password?token=");
        sb.append(credentialReset.getToken());
        sb.append("'>Link</a>");
        sendEmailTemplate(credentialReset.getAccount(), "mail.password.reset.subject",
                "mail.password.reset.body", new Object[] {sb});
    }

    public void sendEmailToChangePasswordByAdmin(CredentialReset credentialReset) {
        StringBuilder sb = new StringBuilder();
        sb.append("<a href='https://team-1.proj-sum.it.p.lodz.pl/login/reset-password?token=");
        sb.append(credentialReset.getToken());
        sb.append("'>Link</a>");
        sendEmailTemplate(credentialReset.getAccount(), "mail.password.changed.by.admin.subject",
                "mail.password.changed.by.admin.body", new Object[] {sb});
    }

    public void sendEmailToChangeEmailByAdmin(ChangeEmail changeEmail, String email) {
        StringBuilder sb = new StringBuilder();
        sb.append("<a href='https://team-1.proj-sum.it.p.lodz.pl/confirm-email?token=");
        sb.append(changeEmail.getToken());
        sb.append("'>Link</a>");
        sendEmailOnNewMail(changeEmail.getAccount(), "mail.email.changed.by.admin.subject",
                "mail.email.changed.by.admin.body", new Object[] {sb}, email);
    }

    public void sendEmailToChangeMyPassword(ChangeMyPassword newResetIssue) {
        StringBuilder sb = new StringBuilder();
        sb.append("<a href='https://team-1.proj-sum.it.p.lodz.pl/");
        sb.append("change-my-password");
        sb.append("?token=");
        sb.append(newResetIssue.getToken());
        sb.append("'>Link</a>");
        sendEmailTemplate(newResetIssue.getAccount(), "mail.password.changed.by.you.subject",
                "mail.password.changed.by.you.body", new Object[] {sb});
    }

    public void sendEmailToPasswordUnauthorized(ResetPasswordUnauthorizedToken resetPasswordUnauthorizedToken) {
        StringBuilder sb = new StringBuilder();
        sb.append("<a href='https://team-1.proj-sum.it.p.lodz.pl/");
        sb.append("change-my-password");
        sb.append("?token=");
        sb.append(resetPasswordUnauthorizedToken.getToken());
        sb.append("'>Link</a>");
        sendEmailTemplate(resetPasswordUnauthorizedToken.getAccount(), "mail.password.changed.by.you.subject",
                "mail.password.changed.by.you.body", new Object[] {sb});
    }

    public void sendEmailToChangeMyEmail(ChangeEmail changeEmail, String email) {
        StringBuilder sb = new StringBuilder();
        sb.append("<a href='https://team-1.proj-sum.it.p.lodz.pl/");
        sb.append("confirm-email");
        sb.append("?token=");
        sb.append(changeEmail.getToken());
        sb.append("'>Link</a>");
        sendEmailOnNewMail(changeEmail.getAccount(), "mail.email.changed.by.you.subject",
                "mail.email.changed.by.you.body", new Object[] {sb}, email);
    }

    public void sendEmailToVerifyAccount(Account account, String randString) {
        StringBuilder sb = new StringBuilder();
        sb.append("<a href='https://team-1.proj-sum.it.p.lodz.pl/verify-account?token=");
        sb.append(randString);
        sb.append("'>Link</a>");
        sendEmailTemplate(account, "mail.verify.account.subject",
                "mail.verify.account.body", new Object[] {sb});
    }

    public void sendEmailToInformAboutVerification(Account account) {
        sendEmailTemplate(account, "mail.after.verify.subject", "mail.after.verify.body", new Object[] {AccountRoleEnum.ROLE_PARTICIPANT});
    }

    public void sendEmailToInformAboutUnblockAccount(Account account) {
        sendEmailTemplate(account, "mail.unblocked.subject", "mail.unblocked.body", null);
    }

    public void sendEmailToUnblockAccountViaLink(Account account, String token) {
        StringBuilder sb = new StringBuilder();
        sb.append("<a href='https://team-1.proj-sum.it.p.lodz.pl/unblock-account?token=");
        sb.append(token);
        sb.append("'>Link</a>");
        sendEmailTemplate(account, "mail.unblock.account.subject", "mail.unblock.account.body", new Object[] {sb});
    }


    public void sendEmailToInformAboutAccountBlocked(Account account) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime lockTimeout = LocalDateTime.now().plusSeconds(config.getAuthLockTime());
        sendEmailTemplate(account, "mail.locked.until.subject", "mail.locked.until.body",
                new Object[] {lockTimeout.format(formatter)});
    }

}
