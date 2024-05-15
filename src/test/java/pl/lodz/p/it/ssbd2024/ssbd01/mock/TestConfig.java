package pl.lodz.p.it.ssbd2024.ssbd01.mock;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.lodz.p.it.ssbd2024.ssbd01.auth.repository.AccountAuthRepository;
import pl.lodz.p.it.ssbd2024.ssbd01.auth.service.AuthenticationService;
import pl.lodz.p.it.ssbd2024.ssbd01.mok.repository.AccountMokRepository;
import pl.lodz.p.it.ssbd2024.ssbd01.mok.service.AccountService;
import pl.lodz.p.it.ssbd2024.ssbd01.mok.service.MeService;

@Configuration
public class TestConfig {

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
    public AccountMokRepository accountMokRepository() {
        return Mockito.mock(AccountMokRepository.class);
    }

    @Bean
    public AccountAuthRepository accountAuthRepository() {
        return Mockito.mock(AccountAuthRepository.class);
    }

}
