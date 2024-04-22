package pl.lodz.p.it.ssbd2024.ssbd01.util.logger;

import org.slf4j.MDC;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

public class LoggerProps {

    public static void addPropsToLogs() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (authentication != null) ? authentication.getName() : "anonymous";
        UUID uuid = UUID.randomUUID();
        MDC.put("transactionId", uuid.toString());
        MDC.put("user", username);
    }

}
