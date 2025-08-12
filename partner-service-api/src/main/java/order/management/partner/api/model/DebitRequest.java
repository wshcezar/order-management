package order.management.partner.api.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record DebitRequest(
        @NotNull(message = "Order value must be provided")
        @Positive(message = "Order value must be positive")
        BigDecimal orderValue
) {}
