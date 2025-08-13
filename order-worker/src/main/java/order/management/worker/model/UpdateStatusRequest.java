package order.management.worker.model;

public record UpdateStatusRequest(
        OrderStatus status
) {}