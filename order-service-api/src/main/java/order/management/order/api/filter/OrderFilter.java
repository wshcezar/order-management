package order.management.order.api.filter;

public sealed interface OrderFilter permits StatusFilter, DateRangeFilter {}

