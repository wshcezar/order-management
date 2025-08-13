package order.management.order.client;

import order.management.order.client.model.PartnerResponse;
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
}