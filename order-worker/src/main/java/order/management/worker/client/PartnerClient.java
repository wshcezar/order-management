package order.management.worker.client;

import order.management.worker.model.DebitRequest;
import order.management.worker.model.PartnerResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.UUID;

@Component
public class PartnerClient {

    private final WebClient webClient;

    public PartnerClient(@Qualifier("partnerWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public PartnerResponse getById(UUID id) {
        return webClient.get()
                .uri("/{id}", id)
                .retrieve()
                .bodyToMono(PartnerResponse.class)
                .block();
    }

    public void debitPartner(UUID id, DebitRequest request) {
        webClient.patch()
                .uri("/{id}/debits", id)
                .bodyValue(request)
                .retrieve()
                .toBodilessEntity()
                .block();
    }
}