package order.management.order.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import order.management.order.api.filter.OrderFilter;
import order.management.order.api.model.OrderRequest;
import order.management.order.api.model.OrderResponse;
import order.management.order.api.model.UpdateStatusRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

@Tag(name = "Order API", description = "Order management operations")
public interface OrderEndpoint {

    @Operation(
            summary = "Create a new order",
            description = "Creates a new order with the provided details.",
            requestBody = @RequestBody(
                    required = true,
                    description = "Order data to be created",
                    content = @Content(schema = @Schema(implementation = OrderRequest.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Order successfully created",
                            content = @Content(schema = @Schema(implementation = OrderResponse.class)))
            }
    )
    ResponseEntity<OrderResponse> create(OrderRequest request);

    @Operation(
            summary = "Update order status",
            description = "Updates the status of an existing order identified by UUID.",
            requestBody = @RequestBody(
                    required = true,
                    description = "New status to set",
                    content = @Content(schema = @Schema(implementation = UpdateStatusRequest.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Order status updated successfully",
                            content = @Content(schema = @Schema(implementation = OrderResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Order not found")
            }
    )
    ResponseEntity<OrderResponse> updateStatus(UUID id, UpdateStatusRequest request);

    @Operation(
            summary = "Get order by ID",
            description = "Retrieves order details by its UUID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Order found",
                            content = @Content(schema = @Schema(implementation = OrderResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Order not found")
            }
    )
    ResponseEntity<OrderResponse> getById(UUID id);

    @Operation(
            summary = "List orders",
            description = "Returns a paginated list of orders, optionally filtered by parameters.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of orders retrieved",
                            content = @Content(schema = @Schema(implementation = Page.class)))
            }
    )
    ResponseEntity<Page<OrderResponse>> getOrders(OrderFilter filter, Pageable pageable);

    @Operation(
            summary = "Cancel order",
            description = "Cancels an existing order by its UUID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Order successfully cancelled"),
                    @ApiResponse(responseCode = "404", description = "Order not found")
            }
    )
    ResponseEntity<Void> cancel(UUID id);
}