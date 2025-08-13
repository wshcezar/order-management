package order.management.worker.model;

import java.math.BigDecimal;

public record DebitRequest(
        BigDecimal orderValue
) {}