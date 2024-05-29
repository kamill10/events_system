package pl.lodz.p.it.ssbd2024.ssbd01.util.logger;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.UUID;

@Aspect
@Component
public class TxAspect {

    protected static final Logger log = LoggerFactory.getLogger(TxAspect.class);
    private static final ThreadLocal<String> transactionIdThreadLocal = new ThreadLocal<>();

    @Autowired
    private Environment env;

    @Pointcut("@annotation(transactional)")
    public void transactionalMethodPointcut(Transactional transactional) {
    }

    @Around(value = "transactionalMethodPointcut(transactional)", argNames = "joinPoint,transactional")
    public Object logTransactions(ProceedingJoinPoint joinPoint, Transactional transactional) throws Throwable {
        if (transactional.propagation() != Propagation.REQUIRES_NEW && transactional.propagation() != Propagation.NESTED
                && transactional.propagation() != Propagation.REQUIRED) {
            return joinPoint.proceed();
        }
        if (!TransactionSynchronizationManager.isActualTransactionActive() || transactionIdThreadLocal.get() == null) {
            generateTransactionId();
        }

        String transactionId = transactionIdThreadLocal.get();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (authentication != null) ? authentication.getName() : "anonymous";
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getName();
        String timeout = resolveTimeout(transactional.timeoutString());
        log.info("Transaction {} started: {} from class: {} by user: {}", transactionId, methodName, className, username);
        log.info("Transaction {} description:\n1. Propagation: {}\n2. Isolation: {}\n3. Timeout: {}\n4. ReadOnly: {}", transactionId,
                transactional.propagation(), transactional.isolation(), timeout, transactional.readOnly());

        Object[] args = joinPoint.getArgs();
        for (int i = 0; i < args.length; i++) {
            log.info("Transaction {} argument[{}]: {}", transactionId, i, args[i]);
        }

        Object proceed;
        try {
            TransactionSynchronizationManager.registerSynchronization(new LoggingTransactionSynchronization(transactionId));
            proceed = joinPoint.proceed();
            log.info("Transaction {} returned: {}", transactionId, proceed);
        } catch (Exception e) {
            log.info("Transaction {} result: rejected with message: {}", transactionId, e.getMessage());
            throw e;
        } finally {
            log.info("Transaction {} ended: {}", transactionId, methodName);
            if (!TransactionSynchronizationManager.isActualTransactionActive()) {
                transactionIdThreadLocal.remove();
            }
        }

        return proceed;
    }

    private void generateTransactionId() {
        transactionIdThreadLocal.set(UUID.randomUUID().toString());
    }

    private String resolveTimeout(String timeoutString) {
        if (timeoutString.startsWith("${") && timeoutString.endsWith("}")) {
            String propertyKey = timeoutString.substring(2, timeoutString.length() - 1);
            return env.getProperty(propertyKey, "default");
        }
        return timeoutString;
    }
}
