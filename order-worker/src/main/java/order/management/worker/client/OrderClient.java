package order.management.worker.client;

import order.management.worker.model.OrderResponse;
import order.management.worker.model.UpdateStatusRequest;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.UUID;

@Component
public class OrderClient {

    private final WebClient webClient;

    public OrderClient(@Qualifier("orderWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public OrderResponse getById(UUID id) {
        return webClient.get()
                .uri("/{id}", id)
                .retrieve()
                .bodyToMono(OrderResponse.class)
                .block();
    }

    public void updateStatus(UUID id, UpdateStatusRequest request) {
        webClient.patch()
                .uri("/{id}/status", id)
                .bodyValue(request)
                .retrieve()
                .toBodilessEntity()
                .block();
    }

    public void orderCancel(UUID id) {
        webClient.delete()
                .uri("/{id}", id)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }
}
