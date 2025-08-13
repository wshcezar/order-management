package order.management.partner.api.controller;

import jakarta.validation.Valid;
import order.management.partner.api.PartnerEndpoint;
import order.management.partner.api.model.CreditRequest;
import order.management.partner.api.model.DebitRequest;
import order.management.partner.api.model.PartnerRequest;
import order.management.partner.api.model.PartnerResponse;
import order.management.partner.service.PartnerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/partners")
public class PartnerController implements PartnerEndpoint {

    private final PartnerService service;

    public PartnerController(PartnerService service) {
        this.service = service;
    }

    @Override
    @PostMapping
    public ResponseEntity<PartnerResponse> create(@Valid @RequestBody PartnerRequest request) {
        return new ResponseEntity<>(service.create(request), HttpStatus.CREATED);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<PartnerResponse> getById(@PathVariable UUID id) {
        return new ResponseEntity<>(service.getById(id), HttpStatus.OK);
    }

    @Override
    @PatchMapping("/{id}/credits")
    public ResponseEntity<Void> addCredit(@PathVariable UUID id, @Valid @RequestBody CreditRequest request) {
        service.addCredit(id, request);
        return ResponseEntity.ok().build();
    }

    @Override
    @PatchMapping("/{id}/debits")
    public ResponseEntity<Void> debitPartner(@PathVariable UUID id, @Valid @RequestBody DebitRequest request) {
        service.debitPartner(id, request);
        return ResponseEntity.ok().build();
    }
}