package pl.lodz.p.it.ssbd2024.ssbd01.util.logger;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.mok.LoginDTO;

@Aspect
@Component
public class AuthAspect {

    private static final Logger log = LoggerFactory.getLogger(AuthAspect.class);

    @Pointcut("execution(* pl.lodz.p.it.ssbd2024.ssbd01.auth.service.AuthenticationService.authenticate(..))")
    private void authenticateMethod() {
    }

    @AfterReturning("authenticateMethod()")
    public void logAuthentication(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        LoginDTO loginDTO = (LoginDTO) args[0];
        String username = loginDTO.username();
        String ipAddress = getUserIpAddress();
        log.info("User: {} logged in from IP: {}", username, ipAddress);
    }

    private String getUserIpAddress() {
        HttpServletRequest curRequest = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        return curRequest.getHeader("X-Forwarded-For") != null ? curRequest.getHeader("X-Forwarded-For") : curRequest.getRemoteAddr();
    }

}