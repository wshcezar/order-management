package order.management.order.api.filter;

import jakarta.validation.constraints.NotNull;
import order.management.order.api.model.OrderStatus;

public record StatusFilter(
        @NotNull(message = "Status is required")
        OrderStatus status
) implements OrderFilter {}
