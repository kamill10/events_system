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
@PropertySource("classpath:mail.properties")
@AllArgsConstructor
public class MailConfig {

    private final Environment env;

    @Bean
    public JavaMailSender getMailSender() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost(env.getProperty("spring.mail.host"));
        javaMailSender.setPort(Integer.parseInt(Objects.requireNonNull(env.getProperty("spring.mail.port"))));
        javaMailSender.setUsername(env.getProperty("spring.mail.username"));
        String password = env.getProperty("spring.mail.password");
        javaMailSender.setPassword(new String(java.util.Base64.getDecoder().decode(password)));
        Properties javaMailProperties = new Properties();
        javaMailProperties.put("mail.smtp.ssl.enable", "true");
        javaMailProperties.put("mail.debug", "true");
        javaMailProperties.put("mail.smtp.ssl.trust", "*");
        javaMailProperties.put("mail.smtp.auth", "true");
        javaMailProperties.put("mail.transport.protocol", "smtp");
        javaMailProperties.put("mail.smtp.connectiontimeout", "5000");
        javaMailProperties.put("mail.smtp.timeout", "3000");

        javaMailSender.setJavaMailProperties(javaMailProperties);
        return javaMailSender;
    }
}