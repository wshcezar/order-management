package order.management.order.service;

import order.management.order.api.filter.DateRangeFilter;
import order.management.order.api.filter.OrderFilter;
import order.management.order.api.filter.StatusFilter;
import order.management.order.api.model.*;
import order.management.order.client.PartnerClient;
import order.management.order.client.model.PartnerResponse;
import order.management.order.exception.PartnerNotFoundException;
import order.management.order.repository.OrderRepository;
import order.management.order.repository.entity.OrderEntity;
import order.management.order.repository.entity.OrderItemEntity;
import order.management.order.repository.specification.OrderSpecifications;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

import static order.management.order.config.RabbitConfig.ORDER_EXCHANGE;
import static order.management.order.config.RabbitConfig.ORDER_QUEUE;

@Service
public class OrderService {

    private final PartnerClient partnerClient;
    private final RabbitTemplate rabbitTemplate;
    private final OrderRepository repository;

    public OrderService(PartnerClient partnerClient, RabbitTemplate rabbitTemplate, OrderRepository repository) {
        this.partnerClient = partnerClient;
        this.rabbitTemplate = rabbitTemplate;
        this.repository = repository;
    }


    @Transactional
    public OrderResponse create(OrderRequest request) {
        PartnerResponse partner;
        try {
            partner = partnerClient.getById(request.partnerId());
        } catch (Exception e) {
            throw new PartnerNotFoundException(request.partnerId());
        }

        var order = new OrderEntity(partner.uuid());
        var items = request.items().stream().map(item ->
                new OrderItemEntity(item.product(), item.quantity(), item.unitPrice(), order)).toList();

        order.setItems(items);
        order.setTotalValue(items.stream()
                .map(i -> i.getUnitPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
        );

        var entity = repository.saveAndFlush(order);
        rabbitTemplate.convertAndSend(ORDER_EXCHANGE, ORDER_QUEUE, entity.getId());

        return toOrderResponse(entity);
    }

    @Transactional
    public OrderResponse updateStatus(UUID id, UpdateStatusRequest request) {
        var order = getOrderById(id);
        order.setStatus(request.status());

        var entity = repository.saveAndFlush(order);
        return toOrderResponse(entity);
    }

    @Transactional(readOnly = true)
    public OrderResponse getById(UUID id) {
        var entity = getOrderById(id);
        return toOrderResponse(entity);
    }

    @Transactional(readOnly = true)
    public Page<OrderResponse> getOrders(OrderFilter filter, Pageable pageable) {
        Specification<OrderEntity> spec = switch (filter) {
            case StatusFilter f -> OrderSpecifications.hasStatus(f.status());
            case DateRangeFilter f -> OrderSpecifications.createdBetween(
                    f.start().atStartOfDay(),
                    f.end().atTime(23, 59, 59)
            );
        };
        return repository.findAll(spec, pageable).map(this::toOrderResponse);
    }


    @Transactional
    public void cancel(UUID id) {
        var entity = getOrderById(id);
        entity.setStatus(OrderStatus.CANCELED);
        repository.saveAndFlush(entity);
    }

    private OrderEntity getOrderById(UUID id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
    }

    private OrderResponse toOrderResponse(OrderEntity entity) {
        var items = entity.getItems().stream().map(this::toItems).toList();
        return new OrderResponse(entity.getId(), entity.getPartnerId(), entity.getCreatedAt(), entity.getUpdatedAt(), entity.getTotalValue(), entity.getStatus(), items);
    }

    private OrderItemRequest toItems(OrderItemEntity entity) {
        return new OrderItemRequest(entity.getProduct(), entity.getQuantity(), entity.getUnitPrice());
    }
}
