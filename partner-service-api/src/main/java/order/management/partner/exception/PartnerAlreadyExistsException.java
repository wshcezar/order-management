package order.management.partner.exception;

public class PartnerAlreadyExistsException extends RuntimeException {
    public PartnerAlreadyExistsException(String name) {
        super("Partner with name '" + name + "' already exists");
    }
}