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
        String transactionId = generateTransactionId();
        log.info("Transaction " + transactionId + " started: " + methodName + " by user: " + username);
        log.info("Transaction " + transactionId + " description:" + "\n1. Propagation: " + transactional.propagation() + "\n2. Isolation: "
                + transactional.isolation() + "\n3. Timeout: " + transactional.timeout() + "\n4. ReadOnly: " + transactional.readOnly());
        Object proceed;
        try {
            proceed = joinPoint.proceed();
            log.info("Transaction " + transactionId + " result: approved");
        } catch (Exception e) {
            log.info("Transaction " + transactionId + " result: rejected with message: " + e.getMessage());
            throw e;
        } finally {
            log.info("Transaction " + transactionId + " ended: " + methodName);
        }

        return proceed;
    }


    private String generateTransactionId() {
        return UUID.randomUUID().toString();
    }
}

