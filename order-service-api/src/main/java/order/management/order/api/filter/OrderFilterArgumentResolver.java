package order.management.order.api.filter;

import jakarta.servlet.http.HttpServletRequest;
import order.management.order.api.model.OrderStatus;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.time.LocalDate;
import java.util.Objects;

@Component
public class OrderFilterArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return OrderFilter.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) {

        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);

        if (Objects.isNull(request))
            throw new IllegalArgumentException("Invalid filter parameters");

        String status = request.getParameter("status");
        String start = request.getParameter("start");
        String end = request.getParameter("end");

        if (status != null) {
            return new StatusFilter(OrderStatus.valueOf(status.toUpperCase()));
        }

        if (start != null && end != null) {
            return new DateRangeFilter(LocalDate.parse(start), LocalDate.parse(end));
        }
        throw new IllegalArgumentException("Invalid filter parameters");
    }
}

