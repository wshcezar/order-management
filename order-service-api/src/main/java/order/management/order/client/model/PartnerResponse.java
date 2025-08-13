package order.management.order.client.model;

import java.math.BigDecimal;
import java.util.UUID;

public record PartnerResponse(
        UUID uuid,
        String name,
        BigDecimal creditLimit
) {}