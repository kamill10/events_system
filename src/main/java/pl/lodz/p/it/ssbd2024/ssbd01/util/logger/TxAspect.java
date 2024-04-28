package pl.lodz.p.it.ssbd2024.ssbd01.util.logger;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Aspect
@Component
public class TxAspect {

    private static final Logger log = LoggerFactory.getLogger(TxAspect.class);

    @Pointcut("@annotation(transactional)")
    public void transactionalMethodPointcut(Transactional transactional) {
    }

    @Around(value = "transactionalMethodPointcut(transactional)", argNames = "joinPoint,transactional")
    public Object logTransactions(ProceedingJoinPoint joinPoint, Transactional transactional) throws Throwable {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (authentication != null) ? authentication.getName() : "anonymous";
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getName();
        String transactionId = generateTransactionId();
        log.info("Transaction {} started: {} from class: {} by user: {}", transactionId, methodName, className, username);
        log.info("Transaction {} description:\n1. Propagation: {}\n2. Isolation: {}\n3. Timeout: {}\n4. ReadOnly: {}", transactionId,
                transactional.propagation(), transactional.isolation(), transactional.timeout(), transactional.readOnly());
        Object proceed;
        try {
            proceed = joinPoint.proceed();
            log.info("Transaction {} result: approved", transactionId);
        } catch (Exception e) {
            log.info("Transaction {} result: rejected with message: {}", transactionId, e.getMessage());
            throw e;
        } finally {
            log.info("Transaction {} ended: {}", transactionId, methodName);
        }

        return proceed;
    }


    private String generateTransactionId() {
        return UUID.randomUUID().toString();
    }
}

