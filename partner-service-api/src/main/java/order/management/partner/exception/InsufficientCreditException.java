package order.management.partner.exception;

import java.math.BigDecimal;
import java.util.UUID;

public class InsufficientCreditException extends RuntimeException {
    public InsufficientCreditException(UUID uuid, BigDecimal currentCredit, BigDecimal debitAmount) {
        super(String.format(
                "Insufficient credit for partner %s. Current: %s, Requested debit: %s",
                uuid, currentCredit, debitAmount
        ));
    }
}