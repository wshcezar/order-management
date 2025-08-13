package order.management.partner.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import order.management.partner.api.model.CreditRequest;
import order.management.partner.api.model.DebitRequest;
import order.management.partner.api.model.PartnerRequest;
import order.management.partner.api.model.PartnerResponse;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

@Tag(name = "Partner API", description = "Partner management")
public interface PartnerEndpoint {

    @Operation(
            summary = "Create a new partner",
            description = "Creates a partner with the specified name and credit limit.",
            requestBody = @RequestBody(
                    required = true,
                    description = "Partner data to create",
                    content = @Content(schema = @Schema(implementation = PartnerRequest.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Partner successfully created",
                            content = @Content(schema = @Schema(implementation = PartnerResponse.class))),
                    @ApiResponse(responseCode = "409", description = "Partner already exists",
                            content = @Content)
            }
    )
    ResponseEntity<PartnerResponse> create(PartnerRequest request);

    @Operation(
            summary = "Get partner by ID",
            description = "Returns partner data identified by the UUID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Partner found",
                            content = @Content(schema = @Schema(implementation = PartnerResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Partner not found",
                            content = @Content)
            }
    )
    ResponseEntity<PartnerResponse> getById(UUID id);

    @Operation(
            summary = "Add credit to partner",
            description = "Increases the partner's credit limit by the specified amount.",
            requestBody = @RequestBody(
                    required = true,
                    description = "Amount to be credited",
                    content = @Content(schema = @Schema(implementation = CreditRequest.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Credit successfully added",
                            content = @Content),
                    @ApiResponse(responseCode = "404", description = "Partner not found",
                            content = @Content)
            }
    )
    ResponseEntity<Void> addCredit(UUID id, CreditRequest request);

    @Operation(
            summary = "Debit credit from partner",
            description = "Decreases the partner's credit limit by the specified amount, if the balance is sufficient.",
            requestBody = @RequestBody(
                    required = true,
                    description = "Amount to be debited",
                    content = @Content(schema = @Schema(implementation = DebitRequest.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Debit successfully performed",
                            content = @Content),
                    @ApiResponse(responseCode = "404", description = "Partner not found",
                            content = @Content),
                    @ApiResponse(responseCode = "422", description = "Insufficient balance for debit",
                            content = @Content)
            }
    )
    ResponseEntity<Void> debitPartner(UUID id, DebitRequest request);
}
