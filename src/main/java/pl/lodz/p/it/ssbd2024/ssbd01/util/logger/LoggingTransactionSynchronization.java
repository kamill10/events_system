package pl.lodz.p.it.ssbd2024.ssbd01.util.logger;

import org.springframework.transaction.support.TransactionSynchronization;

import static pl.lodz.p.it.ssbd2024.ssbd01.util.logger.TxAspect.log;

public class LoggingTransactionSynchronization implements TransactionSynchronization {

    private final String transactionId;

    public LoggingTransactionSynchronization(String transactionId) {
        this.transactionId = transactionId;
    }

    @Override
    public void afterCompletion(int status) {
        String statusMessage = switch (status) {
            case TransactionSynchronization.STATUS_COMMITTED -> "committed";
            case TransactionSynchronization.STATUS_ROLLED_BACK -> "rolled back";
            default -> "unknown";
        };
        log.info("Transaction {} status: {}", transactionId, statusMessage);
    }
}
