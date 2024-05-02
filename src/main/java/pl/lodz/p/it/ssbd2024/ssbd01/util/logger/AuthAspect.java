package pl.lodz.p.it.ssbd2024.ssbd01.util.logger;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.LoginDTO;

@Aspect
@Component
public class AuthAspect {

    private final HttpServletRequest request;

    private static final Logger log = LoggerFactory.getLogger(AuthAspect.class);

    public AuthAspect(HttpServletRequest request) {
        this.request = request;
    }

    @Pointcut("execution(* pl.lodz.p.it.ssbd2024.ssbd01.auth.controller.AuthenticationController.authenticate(..))")
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
        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }
        return ipAddress;
    }

}
