package pl.lodz.p.it.ssbd2024.ssbd01.util;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mok.Account;

@Service
@AllArgsConstructor
public class MailService {

    private JavaMailSender mailSender;

    private void sendEmail(Mail mail) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
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

    public void sendEmail(Account mailTo, String mailSubject, String mailContent) {
        // TODO: Add i18n support, dont hardcode any string but rather use map and user lang preference
        String mailText = "<html> <body> <h2>Hello " + mailTo.getFirstName() + "</h2>" +  "<p> "  + mailContent + " </p> </body> </html>";
        Mail mail = new Mail(mailTo.getEmail(), mailSubject, mailText);

        sendEmail(mail);
    }

}
