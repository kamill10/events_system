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
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.UUID;

@Aspect
@Component
public class TxAspect {

    protected static final Logger log = LoggerFactory.getLogger(TxAspect.class);
    private static String transactionId;

    @Pointcut("@annotation(transactional)")
    public void transactionalMethodPointcut(Transactional transactional) {
    }

    @Around(value = "transactionalMethodPointcut(transactional)", argNames = "joinPoint,transactional")
    public Object logTransactions(ProceedingJoinPoint joinPoint, Transactional transactional) throws Throwable {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (authentication != null) ? authentication.getName() : "anonymous";
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getName();
        generateTransactionId();
        log.info("Transaction {} started: {} from class: {} by user: {}", transactionId, methodName, className, username);
        log.info("Transaction {} description:\n1. Propagation: {}\n2. Isolation: {}\n3. Timeout: {}\n4. ReadOnly: {}", transactionId,
                transactional.propagation(), transactional.isolation(), transactional.timeout(), transactional.readOnly());
        Object[] args = joinPoint.getArgs();
        for (int i = 0; i < args.length; i++) {
            log.info("Transaction {} argument[{}]: {}", transactionId, i, args[i]);
        }
        Object proceed;
        try {
            TransactionSynchronizationManager.registerSynchronization(new LoggingTransactionSynchronization(transactionId));
            proceed = joinPoint.proceed();
            log.info("Transaction {} returned: {}", transactionId, proceed);
        } finally {
            log.info("Transaction {} ended: {}", transactionId, methodName);
        }

        return proceed;
    }


    private void generateTransactionId() {
        transactionId = UUID.randomUUID().toString();
    }
}

