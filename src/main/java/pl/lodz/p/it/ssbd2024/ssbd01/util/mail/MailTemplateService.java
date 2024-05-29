package pl.lodz.p.it.ssbd2024.ssbd01.util.mail;

import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mok.Account;

import java.util.Locale;

@Service
@AllArgsConstructor
@Transactional(propagation = Propagation.MANDATORY)
@PreAuthorize("hasRole('ROLE_SYSTEM')")
public class MailTemplateService {

    private MessageSource messageSource;
    private final MailSenderService mailSenderService;

    public void sendEmailTemplate(Account mailTo, String mailSubject, String mailContent, Object[] contentArgs) {
        Locale locale = Locale.forLanguageTag(mailTo.getLanguage().getLanguageCode());
        String subject = messageSource.getMessage(mailSubject, null, locale);
        String mailBody = messageSource.getMessage(mailContent, contentArgs, locale);
        String name = messageSource.getMessage("mail.hello", new Object[] {mailTo.getFirstName()}, locale);
        String mailText = "<html> <body> <h2> " + name + "</h2>" + "<p> " + mailBody + " </p> </body> </html>";
        Mail mail = new Mail(mailTo.getEmail(), subject, mailText);

        mailSenderService.sendEmail(mail);
    }

    public void sendEmailOnNewMail(Account mailTo, String mailSubject, String mailContent, Object[] contentArgs, String newEmail) {
        Locale locale = Locale.forLanguageTag(mailTo.getLanguage().getLanguageCode());
        String subject = messageSource.getMessage(mailSubject, null, locale);
        String mailBody = messageSource.getMessage(mailContent, contentArgs, locale);
        String name = messageSource.getMessage("mail.hello", new Object[] {mailTo.getFirstName()}, locale);
        String mailText = "<html> <body> <h2> " + name + "</h2>" + "<p> " + mailBody + " </p> </body> </html>";
        Mail mail = new Mail(newEmail, subject, mailText);

        mailSenderService.sendEmail(mail);
    }
}
