package pl.lodz.p.it.ssbd2024.ssbd01.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Objects;
import java.util.Properties;

@Configuration
@AllArgsConstructor
public class MailConfig {

    private ConfigurationProperties config;

    @Bean
    public JavaMailSender getMailSender() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost(config.getMailHost());
        javaMailSender.setPort(config.getMailPort());
        javaMailSender.setUsername(config.getMailUsername());
        String password = config.getMailPassword();
        javaMailSender.setPassword(new String(java.util.Base64.getDecoder().decode(password)));
        Properties javaMailProperties = new Properties();
        javaMailProperties.put("mail.smtp.ssl.enable", config.getMailSslEnable());
        javaMailProperties.put("mail.debug", config.getMailDebug());
        javaMailProperties.put("mail.smtp.ssl.trust", config.getMailSslTrust());
        javaMailProperties.put("mail.smtp.auth", config.getMailAuth());
        javaMailProperties.put("mail.transport.protocol", config.getMailProtocol());
        javaMailProperties.put("mail.smtp.connectiontimeout", config.getMailConnectionTimeout());
        javaMailProperties.put("mail.smtp.timeout", config.getMailTimeout());

        javaMailSender.setJavaMailProperties(javaMailProperties);
        return javaMailSender;
    }
}