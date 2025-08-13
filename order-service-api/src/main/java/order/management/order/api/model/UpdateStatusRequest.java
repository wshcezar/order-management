package order.management.order.api.model;

import jakarta.validation.constraints.NotNull;

public record UpdateStatusRequest(
        @NotNull(message = "Status must not be blank")
        OrderStatus status
) {}
