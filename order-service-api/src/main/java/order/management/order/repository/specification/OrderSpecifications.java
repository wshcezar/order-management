package order.management.order.repository.specification;

import order.management.order.api.model.OrderStatus;
import order.management.order.repository.entity.OrderEntity;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class OrderSpecifications {

    public static Specification<OrderEntity> hasStatus(OrderStatus status) {
        return (root, query, cb) ->
                status == null ? cb.conjunction() : cb.equal(root.get("status"), status);
    }

    public static Specification<OrderEntity> createdBetween(LocalDateTime start, LocalDateTime end) {
        return (root, query, cb) -> {
            if (start != null && end != null) {
                return cb.between(root.get("createdAt"), start, end);
            } else if (start != null) {
                return cb.greaterThanOrEqualTo(root.get("createdAt"), start);
            } else if (end != null) {
                return cb.lessThanOrEqualTo(root.get("createdAt"), end);
            }
            return cb.conjunction();
        };
    }
}
