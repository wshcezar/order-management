package order.management.order.api.filter;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record DateRangeFilter(
        @NotNull(message = "Start date is required")
        LocalDate start,

        @NotNull(message = "End date is required")
        LocalDate end
) implements OrderFilter {
        @AssertTrue(message = "Start date must be before or equal to end date.")
        public boolean isStartBeforeEnd() {
                if (start != null && end != null) {
                        return !start.isAfter(end);
                }
                return true;
        }
}
