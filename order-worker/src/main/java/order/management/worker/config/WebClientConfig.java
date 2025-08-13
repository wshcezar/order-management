package order.management.worker.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    private final String orderServiceUrl;
    private final String partnerServiceUrl;

    public WebClientConfig(
            @Value("${order.service.url}") String orderServiceUrl,
            @Value("${partner.service.url}") String partnerServiceUrl) {
        this.orderServiceUrl = orderServiceUrl;
        this.partnerServiceUrl = partnerServiceUrl;
    }

    @Bean(name = "orderWebClient")
    public WebClient orderWebClient() {
        return WebClient.builder()
                .baseUrl(orderServiceUrl)
                .build();
    }

    @Bean(name = "partnerWebClient")
    public WebClient partnerWebClient() {
        return WebClient.builder()
                .baseUrl(partnerServiceUrl)
                .build();
    }
}