package order.management.worker.model;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderResponse(
        UUID orderId,
        UUID partnerId,
        BigDecimal totalAmount,
        OrderStatus status
) {}
