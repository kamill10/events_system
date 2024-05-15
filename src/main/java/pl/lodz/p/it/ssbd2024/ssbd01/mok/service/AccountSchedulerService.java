package pl.lodz.p.it.ssbd2024.ssbd01.mok.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pl.lodz.p.it.ssbd2024.ssbd01.util.RunAs;

@Service
@RequiredArgsConstructor
public class AccountSchedulerService {

    private final AccountService accountService;

    @Scheduled(fixedRate = 120000)
    public void executeDeleteExpiredChangeEmailTokens() {
        RunAs.runAsSystem(accountService::deleteExpiredChangeEmailTokens);
    }

    @Scheduled(fixedRate = 120000)
    public void executeDeleteExpiredChangePasswordTokens() {
        RunAs.runAsSystem(accountService::deleteExpiredChangePasswordTokens);
    }

    @Scheduled(fixedRate = 120000)
    public void executeDeleteExpiredResetCredentialsTokens() {
        RunAs.runAsSystem(accountService::deleteExpiredResetCredentialTokens);
    }

}
