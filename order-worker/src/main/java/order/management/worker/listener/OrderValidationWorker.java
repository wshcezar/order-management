package order.management.worker.listener;

import order.management.worker.client.OrderClient;
import order.management.worker.client.PartnerClient;
import order.management.worker.model.DebitRequest;
import order.management.worker.model.OrderResponse;
import order.management.worker.model.OrderStatus;
import order.management.worker.model.UpdateStatusRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static order.management.worker.config.RabbitConfig.ORDER_QUEUE;

@Service
public class OrderValidationWorker {

    private static final Logger log = LoggerFactory.getLogger(OrderValidationWorker.class);

    private final OrderClient orderClient;
    private final PartnerClient partnerClient;

    public OrderValidationWorker(OrderClient orderClient, PartnerClient partnerClient) {
        this.orderClient = orderClient;
        this.partnerClient = partnerClient;
    }

    @RabbitListener(queues = ORDER_QUEUE)
    public void validateOrder(UUID orderId) {
        log.info("[ORDER_VALIDATION] Starting validation for orderId={}", orderId);

        OrderResponse order;
        try {
            order = orderClient.getById(orderId);
        } catch (Exception e) {
            log.error("[ORDER_VALIDATION] Failed to fetch orderId={} - Error: {}", orderId, e.getMessage(), e);
            return;
        }

        if (order == null) {
            log.warn("[ORDER_VALIDATION] Order not found: orderId={}", orderId);
            return;
        }

        try {
            orderClient.updateStatus(order.orderId(), new UpdateStatusRequest(OrderStatus.DELIVERED));
            log.debug("[ORDER_VALIDATION] process in DELIVERED orderId={}", orderId);
            var partner = partnerClient.getById(order.partnerId());
            if (partner == null) {
                log.warn("[ORDER_VALIDATION] Partner not found: partnerId={} for orderId={}",
                        order.partnerId(), orderId);
                orderClient.orderCancel(order.orderId());
                return;
            }

            log.debug("[ORDER_VALIDATION] orderId={} partnerId={} creditLimit={} orderAmount={}",
                    orderId, partner.uuid(), partner.creditLimit(), order.totalAmount());

            if (partner.creditLimit().compareTo(order.totalAmount()) >= 0) {
                orderClient.updateStatus(order.orderId(), new UpdateStatusRequest(OrderStatus.APPROVED));
                partnerClient.debitPartner(partner.uuid(), new DebitRequest(order.totalAmount()));
                log.info("[ORDER_VALIDATION] Order approved: orderId={} partnerId={} amount={}",
                        orderId, partner.uuid(), order.totalAmount());
            } else {
                orderClient.updateStatus(order.orderId(), new UpdateStatusRequest(OrderStatus.PENDING));
                log.info("[ORDER_VALIDATION] Order rejected (insufficient credit): orderId={} partnerId={} creditLimit={} orderAmount={}",
                        orderId, partner.uuid(), partner.creditLimit(), order.totalAmount());
            }

        } catch (Exception e) {
            orderClient.orderCancel(order.orderId());
            log.error("[ORDER_VALIDATION] Order cancel unexpected error processing orderId={} - Error: {}",
                    orderId, e.getMessage(), e);
        }
        log.info("[ORDER_VALIDATION] Finished processing orderId={}", orderId);
    }
}