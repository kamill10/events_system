package pl.lodz.p.it.ssbd2024.ssbd01.util.logger;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class MethodAspect {

    protected static final Logger log = LoggerFactory.getLogger(MethodAspect.class);

    @Pointcut("execution(* pl.lodz.p.it.ssbd2024.ssbd01.mok.service.*.*(..)) "
            + "|| execution(* pl.lodz.p.it.ssbd2024.ssbd01.mok.repository.*.*(..)) "
            + "|| execution(* pl.lodz.p.it.ssbd2024.ssbd01.mok.controller.*.*(..))"
            + "|| execution(* pl.lodz.p.it.ssbd2024.ssbd01.auth.repository.*.*(..)) "
            + "|| execution(* pl.lodz.p.it.ssbd2024.ssbd01.auth.service.*.*(..))"
            + "|| execution(* pl.lodz.p.it.ssbd2024.ssbd01.auth.controller.*.*(..))"
    )
    private void methodPointcut() {
    }

    @Around("methodPointcut()")
    public Object logMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (authentication != null) ? authentication.getName() : "anonymous";
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getName();
        log.info("Method: {} from class: {} by user: {} ",
                methodName, className, username);
        Object[] args = joinPoint.getArgs();
        for (int i = 0; i < args.length; i++) {
            log.info("Method {} argument[{}]: {}", methodName, i, args[i]);
        }
        Object proceed;
        try {
            proceed = joinPoint.proceed();
            log.info("Method {} returned: {}", methodName, proceed);
        } catch (Exception e) {
            log.info("Method {} result: rejected with message: {}", methodName, e.getMessage());
            throw e;
        } finally {
            log.info("Method ended {}", methodName);
        }

        return proceed;
    }


}
