package order.management.partner.api.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record PartnerRequest(
        @NotBlank(message = "Name must not be blank")
        String name,

        @NotNull(message = "Credit limit must be provided")
        @Positive(message = "Credit limit must be positive")
        BigDecimal creditLimit
) {}
