package pl.lodz.p.it.ssbd2024.ssbd01.util.mail;

import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.ssbd2024.ssbd01.util.Mail;

import java.util.logging.Logger;

@Service
@AllArgsConstructor
public class MailSenderService {

    private JavaMailSender mailSender;

    @Async
    public void sendEmail(Mail mail) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            mimeMessageHelper.setSubject(mail.getMailSubject());
            mimeMessageHelper.setFrom(new InternetAddress(mail.getMailFrom()));
            mimeMessageHelper.setTo(mail.getMailTo());
            mimeMessageHelper.setText(mail.getMailContent(), true);
            //  mailSender.send(mimeMessageHelper.getMimeMessage());
        } catch (Exception e) {
            Logger.getGlobal().severe(e.getMessage());
        }
    }
}
