package order.management.order.config;

import order.management.order.api.filter.OrderFilterArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final OrderFilterArgumentResolver filterResolver;

    public WebConfig(OrderFilterArgumentResolver filterResolver) {
        this.filterResolver = filterResolver;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(filterResolver);
    }
}

