package pl.lodz.p.it.ssbd2024.ssbd01.util;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mok.Account;

import java.util.Locale;

@Service
@AllArgsConstructor
public class MailService {

    private JavaMailSender mailSender;
    private MessageSource messageSource;

    private void sendEmail(Mail mail) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            mimeMessageHelper.setSubject(mail.getMailSubject());
            mimeMessageHelper.setFrom(new InternetAddress(mail.getMailFrom()));
            mimeMessageHelper.setTo(mail.getMailTo());
            mimeMessageHelper.setText(mail.getMailContent(), true);
            mailSender.send(mimeMessageHelper.getMimeMessage());
        } catch (MessagingException e) {
            //TODO: Add logging and remove stack trace
            e.printStackTrace();
        }
    }

    public void sendEmail(Account mailTo, String mailSubject, String mailContent, Object[] contentArgs) {
        Locale locale = Locale.forLanguageTag(mailTo.getLanguage().getLanguageCode());
        String subject = messageSource.getMessage(mailSubject, null, locale);
        String mailBody = messageSource.getMessage(mailContent, contentArgs, locale);
        String name = messageSource.getMessage("mail.hello", new Object[]{mailTo.getFirstName()}, locale);
        String mailText = "<html> <body> <h2> " + name + "</h2>" +  "<p> "  + mailBody + " </p> </body> </html>";
        Mail mail = new Mail(mailTo.getEmail(), subject, mailText);

        sendEmail(mail);
    }

    public void sendEmailOnNewMail(Account mailTo, String mailSubject, String mailContent, Object[] contentArgs,String newEmail) {
        Locale locale = Locale.forLanguageTag(mailTo.getLanguage().getLanguageCode());
        String subject = messageSource.getMessage(mailSubject, null, locale);
        String mailBody = messageSource.getMessage(mailContent, contentArgs, locale);
        String name = messageSource.getMessage("mail.hello", new Object[]{mailTo.getFirstName()}, locale);
        String mailText = "<html> <body> <h2> " + name + "</h2>" +  "<p> "  + mailBody + " </p> </body> </html>";
        Mail mail = new Mail(newEmail, subject, mailText);

        sendEmail(mail);
    }

}
