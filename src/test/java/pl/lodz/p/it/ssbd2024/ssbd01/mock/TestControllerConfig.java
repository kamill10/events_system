package pl.lodz.p.it.ssbd2024.ssbd01.mock;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.HandlerExceptionResolver;
import pl.lodz.p.it.ssbd2024.ssbd01.auth.service.AuthenticationService;
import pl.lodz.p.it.ssbd2024.ssbd01.config.BusinessConfig;
import pl.lodz.p.it.ssbd2024.ssbd01.config.ToolsConfig;
import pl.lodz.p.it.ssbd2024.ssbd01.config.WebCoreConfig;
import pl.lodz.p.it.ssbd2024.ssbd01.mok.service.AccountService;
import pl.lodz.p.it.ssbd2024.ssbd01.mok.service.MeService;
import pl.lodz.p.it.ssbd2024.ssbd01.util.MailService;

@Configuration
@Import({
        WebCoreConfig.class
})
public class TestControllerConfig {

    @Bean
    public AccountService accountService() {
        return Mockito.mock(AccountService.class);
    }

    @Bean
    public AuthenticationService authenticationService() {
        return Mockito.mock(AuthenticationService.class);
    }

    @Bean
    public MeService meService() {
        return Mockito.mock(MeService.class);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return Mockito.mock(PasswordEncoder.class);
    }

    @Bean
    public MailService mailService() {
        return Mockito.mock(MailService.class);
    }


}
