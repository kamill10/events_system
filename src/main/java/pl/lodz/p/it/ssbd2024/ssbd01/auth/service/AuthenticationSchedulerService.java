package pl.lodz.p.it.ssbd2024.ssbd01.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pl.lodz.p.it.ssbd2024.ssbd01.util.MailService;
import pl.lodz.p.it.ssbd2024.ssbd01.util.RunAs;

@Service
@RequiredArgsConstructor
public class AuthenticationSchedulerService {

    private final AuthenticationService authenticationService;
    private final MailService mailService;

    @Scheduled(fixedRate = 120000)
    public void executeDeleteExpiredTokensAndAccounts() {
        RunAs.runAsSystem(authenticationService::deleteExpiredTokensAndAccounts);
    }

    @Scheduled(fixedRate = 120000)
    public void executeDeleteExpiredJWTTokensFromWhitelist() {
        RunAs.runAsSystem(authenticationService::deleteExpiredJWTTokensFromWhitelist);
    }

    @Scheduled(fixedRate = 120000)
    public void executeSendAccountConfirmationReminder() {
        RunAs.runAsSystem(authenticationService::sendAccountConfirmationReminder);
    }

    @Scheduled(fixedRate = 120000)
    public void executeUnlockAccounts() {
        RunAs.runAsSystem(authenticationService::unlockAccounts);
    }

    @Scheduled(fixedRate = 3600000)
    public void executeLockAccountsThatAreNotUsed() {
        RunAs.runAsSystem(authenticationService::lockAccountsThatAreNotUsed);
    }

}
