package order.management.order.exception;

import java.util.UUID;

public class PartnerNotFoundException extends RuntimeException {
    public PartnerNotFoundException(UUID uuid) {
        super("Partner with UUID '" + uuid + "' not found");
    }
}