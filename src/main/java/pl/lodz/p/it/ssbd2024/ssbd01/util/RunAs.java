package pl.lodz.p.it.ssbd2024.ssbd01.util;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.logging.Logger;

public class RunAs {

    @FunctionalInterface
    public interface RunAsMethod {
        default void run() {
            try {
                runWithException();
            } catch (Exception e) {
                Logger.getGlobal().severe(e.getMessage());
            }
        }

        void runWithException() throws Exception;
    }

    public static void runAsSystem(final RunAsMethod func) {
        final AnonymousAuthenticationToken token = new AnonymousAuthenticationToken("system", "system",
                List.of(new SimpleGrantedAuthority("ROLE_SYSTEM")));
        final Authentication originalAuthentication = SecurityContextHolder.getContext().getAuthentication();
        SecurityContextHolder.getContext().setAuthentication(token);
        func.run();
        SecurityContextHolder.getContext().setAuthentication(originalAuthentication);
    }
}
