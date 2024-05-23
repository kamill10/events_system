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

    @Scheduled(fixedRateString = "${scheduler.task.time-rate}")
    public void executeDeleteExpiredTokensAndAccounts() {
        RunAs.runAsSystem(authenticationService::deleteExpiredTokensAndAccounts);
    }

    @Scheduled(fixedRateString = "${scheduler.task.time-rate}")
    public void executeDeleteExpiredJWTTokensFromWhitelist() {
        RunAs.runAsSystem(authenticationService::deleteExpiredJWTTokensFromWhitelist);
    }

    @Scheduled(fixedRateString = "${scheduler.task.time-rate}")
    public void executeSendAccountConfirmationReminder() {
        RunAs.runAsSystem(authenticationService::sendAccountConfirmationReminder);
    }

    @Scheduled(fixedRateString = "${scheduler.task.time-rate}")
    public void executeUnlockAccounts() {
        RunAs.runAsSystem(authenticationService::unlockAccounts);
    }

    @Scheduled(fixedRateString = "${scheduler.task.time-rate.longer}")
    public void executeLockAccountsThatAreNotUsed() {
        RunAs.runAsSystem(authenticationService::lockAccountsThatAreNotUsed);
    }

}
