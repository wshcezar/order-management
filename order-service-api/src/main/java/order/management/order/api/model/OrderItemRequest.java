package order.management.order.api.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record OrderItemRequest(

        @NotBlank(message = "Product must not be blank")
        String product,

        @PositiveOrZero(message = "Quantity must be positive and major zero")
        int quantity,

        @NotNull(message = "Unit price must be provided")
        @Positive(message = "Unit price must be positive")
        BigDecimal unitPrice
) {}