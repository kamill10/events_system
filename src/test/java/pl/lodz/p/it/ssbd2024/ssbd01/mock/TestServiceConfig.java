package pl.lodz.p.it.ssbd2024.ssbd01.mock;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import pl.lodz.p.it.ssbd2024.ssbd01.auth.repository.AccountAuthRepository;
import pl.lodz.p.it.ssbd2024.ssbd01.auth.repository.JWTWhitelistRepository;
import pl.lodz.p.it.ssbd2024.ssbd01.auth.service.AuthenticationSchedulerService;
import pl.lodz.p.it.ssbd2024.ssbd01.config.BusinessConfig;
import pl.lodz.p.it.ssbd2024.ssbd01.config.ConfigurationProperties;
import pl.lodz.p.it.ssbd2024.ssbd01.config.ToolsConfig;
import pl.lodz.p.it.ssbd2024.ssbd01.config.WebCoreConfig;
import pl.lodz.p.it.ssbd2024.ssbd01.config.security.JwtService;
import pl.lodz.p.it.ssbd2024.ssbd01.mok.repository.*;
import pl.lodz.p.it.ssbd2024.ssbd01.mok.service.AccountSchedulerService;
import pl.lodz.p.it.ssbd2024.ssbd01.util.MailService;
import pl.lodz.p.it.ssbd2024.ssbd01.util.ServiceVerifier;
import pl.lodz.p.it.ssbd2024.ssbd01.util.logger.LoggingTransactionSynchronization;

@Configuration
@Import({
        BusinessConfig.class,
        WebCoreConfig.class,
        ConfigurationProperties.class,
        ServiceVerifier.class
})
public class TestServiceConfig {

    @Bean
    public AccountMokRepository accountMokRepository() {
        return Mockito.mock(AccountMokRepository.class);
    }

    @Bean
    public AccountAuthRepository accountAuthRepository() {
        return Mockito.mock(AccountAuthRepository.class);
    }

    @Bean
    public JWTWhitelistRepository jwtWhitelistRepository() {
        return Mockito.mock(JWTWhitelistRepository.class);
    }


    @Bean
    public AccountConfirmationRepository accountConfirmationRepository() {
        return Mockito.mock(AccountConfirmationRepository.class);
    }

    @Bean
    public AuthenticationSchedulerService authenticationSchedulerService() {
        return Mockito.mock(AuthenticationSchedulerService.class);
    }

    @Bean
    public ConfirmationReminderRepository confirmationReminderRepository() {
        return Mockito.mock(ConfirmationReminderRepository.class);
    }

    @Bean
    public AccountSchedulerService accountSchedulerService() {
        return Mockito.mock(AccountSchedulerService.class);
    }

    @Bean
    public ChangeEmailRepository changeMyEmailRepository() {
        return Mockito.mock(ChangeEmailRepository.class);
    }

    @Bean
    public ChangeMyPasswordRepository changeMyPasswordRepository() {
        return Mockito.mock(ChangeMyPasswordRepository.class);
    }

    @Bean
    public CredentialResetRepository credentialResetRepository() {
        return Mockito.mock(CredentialResetRepository.class);
    }

    @Bean
    public PasswordHistoryRepository passwordHistoryRepository() {
        return Mockito.mock(PasswordHistoryRepository.class);
    }

    @Bean
    public RoleRepository roleRepository() {
        return Mockito.mock(RoleRepository.class);
    }

    @Bean
    public JwtService jwtService() {
        return Mockito.mock(JwtService.class);
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return Mockito.mock(AuthenticationManager.class);
    }

    @Bean
    public MailService mailService() {
        return Mockito.mock(MailService.class);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return Mockito.mock(PasswordEncoder.class);
    }

    @Bean
    public AccountThemeRepository accountThemeRepository() {
        return Mockito.mock(AccountThemeRepository.class);
    }

    @Bean
    public TimeZoneRepository accountTimeZoneRepository() {
        return Mockito.mock(TimeZoneRepository.class);
    }

    @Bean
    public AccountUnlockRepository accountUnlockRepository() {
        return Mockito.mock(AccountUnlockRepository.class);
    }



}
