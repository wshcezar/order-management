package order.management.worker.model;

import java.math.BigDecimal;
import java.util.UUID;

public record PartnerResponse(
        UUID uuid,
        String name,
        BigDecimal creditLimit
) {}