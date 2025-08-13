package order.management.order.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    private final String partnerServiceUrl;

    public WebClientConfig(
            @Value("${partner.service.url}") String partnerServiceUrl) {
        this.partnerServiceUrl = partnerServiceUrl;
    }

    @Bean(name = "partnerWebClient")
    public WebClient partnerWebClient() {
        return WebClient.builder()
                .baseUrl(partnerServiceUrl)
                .build();
    }
}