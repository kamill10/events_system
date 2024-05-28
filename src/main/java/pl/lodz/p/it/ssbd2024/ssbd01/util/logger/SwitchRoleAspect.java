package pl.lodz.p.it.ssbd2024.ssbd01.util.logger;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import pl.lodz.p.it.ssbd2024.ssbd01.util._enum.AccountRoleEnum;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mok.Account;

@Aspect
@Component
public class SwitchRoleAspect {
    private final HttpServletRequest request;

    private static final Logger log = LoggerFactory.getLogger(SwitchRoleAspect.class);

    public SwitchRoleAspect(HttpServletRequest request) {
        this.request = request;
    }

    @Pointcut("execution(* pl.lodz.p.it.ssbd2024.ssbd01.mok.service.MeService.logSwitchRole(..))")
    private void switchRoleMethod() {
    }

    @AfterReturning("switchRoleMethod()")
    public void logAuthentication(JoinPoint joinPoint) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account account = (Account) authentication.getPrincipal();
        Object[] args = joinPoint.getArgs();
        AccountRoleEnum role = (AccountRoleEnum) args[0];
        String ipAddress = getUserIpAddress();
        log.info("User: {} switch to role : {} logged in from IP: {}", account.getUsername(),role.toString(), ipAddress);
    }

    private String getUserIpAddress() {
        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }
        return ipAddress;
    }

}
