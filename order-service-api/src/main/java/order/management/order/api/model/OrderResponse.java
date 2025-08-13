package order.management.order.api.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record OrderResponse(
        UUID orderId,
        UUID partnerId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        BigDecimal totalAmount,
        OrderStatus status,
        List<OrderItemRequest> items
) {}
