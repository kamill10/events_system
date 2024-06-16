package pl.lodz.p.it.ssbd2024.ssbd01.mock;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.HandlerExceptionResolver;
import pl.lodz.p.it.ssbd2024.ssbd01.auth.repository.AccountAuthHistoryRepository;
import pl.lodz.p.it.ssbd2024.ssbd01.auth.repository.AccountAuthRepository;
import pl.lodz.p.it.ssbd2024.ssbd01.auth.repository.JWTWhitelistRepository;
import pl.lodz.p.it.ssbd2024.ssbd01.auth.service.AuthenticationSchedulerService;
import pl.lodz.p.it.ssbd2024.ssbd01.auth.service.JwtService;
import pl.lodz.p.it.ssbd2024.ssbd01.config.BusinessConfig;
import pl.lodz.p.it.ssbd2024.ssbd01.config.ConfigurationProperties;
import pl.lodz.p.it.ssbd2024.ssbd01.config.WebCoreConfig;
import pl.lodz.p.it.ssbd2024.ssbd01.mok.repository.*;
import pl.lodz.p.it.ssbd2024.ssbd01.mok.service.AccountSchedulerService;
import pl.lodz.p.it.ssbd2024.ssbd01.mok.service.ServiceVerifier;
import pl.lodz.p.it.ssbd2024.ssbd01.mow.repository.*;
import pl.lodz.p.it.ssbd2024.ssbd01.util.mail.MailService;

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
    public ThemeRepository accountThemeRepository() {
        return Mockito.mock(ThemeRepository.class);
    }

    @Bean
    public TimeZoneRepository accountTimeZoneRepository() {
        return Mockito.mock(TimeZoneRepository.class);
    }

    @Bean
    public AccountUnlockRepository accountUnlockRepository() {
        return Mockito.mock(AccountUnlockRepository.class);
    }

    @Bean
    public AccountMokHistoryRepository accountHistoryRepository() {
        return Mockito.mock(AccountMokHistoryRepository.class);
    }

    @Bean
    public AccountAuthHistoryRepository accountAuthHistoryRepository() {
        return Mockito.mock(AccountAuthHistoryRepository.class);
    }

    @Bean
    public HandlerExceptionResolver handlerExceptionResolver() {
        return Mockito.mock(HandlerExceptionResolver.class);
    }

    @Bean
    public AccountMowRepository accountMowRepository() {
        return Mockito.mock(AccountMowRepository.class);
    }

    @Bean
    public EventRepository eventRepository() {
        return Mockito.mock(EventRepository.class);
    }

    @Bean
    public LocationRepository locationRepository() {
        return Mockito.mock(LocationRepository.class);
    }

    @Bean
    public RoomRepository roomRepository() {
        return Mockito.mock(RoomRepository.class);
    }

    @Bean
    public SessionRepository sessionRepository() {
        return Mockito.mock(SessionRepository.class);
    }

    @Bean
    public SpeakerRepository speakerRepository() {
        return Mockito.mock(SpeakerRepository.class);
    }

    @Bean
    public TicketRepository ticketRepository() {
        return Mockito.mock(TicketRepository.class);
    }

    @Bean SpeakerHistoryRepository speakerHistoryRepository() {
        return Mockito.mock(SpeakerHistoryRepository.class);
    }

}
