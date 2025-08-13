package order.management.order.api.model;

import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record OrderRequest(

        @NotNull(message = "PartnerId must not be blank")
        UUID partnerId,

        @NotNull(message = "Items must not be blank")
        List<OrderItemRequest> items
) {}