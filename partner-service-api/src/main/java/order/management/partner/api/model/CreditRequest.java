package order.management.partner.api.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record CreditRequest(
        @NotNull(message = "Credit value must be provided")
        @Positive(message = "Credit value must be positive")
        BigDecimal creditValue
) {}
