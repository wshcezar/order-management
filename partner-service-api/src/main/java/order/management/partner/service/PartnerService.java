package order.management.partner.service;

import order.management.partner.api.model.CreditRequest;
import order.management.partner.api.model.DebitRequest;
import order.management.partner.api.model.PartnerRequest;
import order.management.partner.api.model.PartnerResponse;
import order.management.partner.exception.InsufficientCreditException;
import order.management.partner.exception.PartnerAlreadyExistsException;
import order.management.partner.exception.PartnerNotFoundException;
import order.management.partner.repository.PartnerRepository;
import order.management.partner.repository.entity.PartnerEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class PartnerService {

    private final PartnerRepository repository;

    public PartnerService(PartnerRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public PartnerResponse create(PartnerRequest request) {
        repository.findByName(request.name())
                .ifPresent(existing -> {
                    throw new PartnerAlreadyExistsException(request.name());
                });
        var entity = new PartnerEntity(request.name(), request.creditLimit());
        var saved = repository.saveAndFlush(entity);
        return toPartnerResponse(saved);
    }

    @Transactional(readOnly = true)
    public PartnerResponse getById(UUID uuid) {
        return toPartnerResponse(findPartnerByUuid(uuid));
    }

    @Transactional
    public void addCredit(UUID uuid, CreditRequest request) {
        PartnerEntity partner = findPartnerByUuid(uuid);
        partner.setCreditLimit(partner.getCreditLimit().add(request.creditValue()));
        repository.saveAndFlush(partner);
    }

    @Transactional
    public void debitPartner(UUID uuid, DebitRequest request) {
        PartnerEntity partner = findPartnerByUuid(uuid);
        if (partner.getCreditLimit().compareTo(request.orderValue()) < 0) {
            throw new InsufficientCreditException(uuid, partner.getCreditLimit(), request.orderValue());
        }
        partner.setCreditLimit(partner.getCreditLimit().subtract(request.orderValue()));
        repository.saveAndFlush(partner);
    }


    private PartnerEntity findPartnerByUuid(UUID uuid) {
        return repository.findByUuid(uuid)
                .orElseThrow(() -> new PartnerNotFoundException(uuid));
    }

    private PartnerResponse toPartnerResponse(PartnerEntity entity) {
        return new PartnerResponse(entity.getUuid(), entity.getName(), entity.getCreditLimit());
    }
}
