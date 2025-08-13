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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PartnerServiceTest {

    @Mock
    private PartnerRepository repository;

    @InjectMocks
    private PartnerService service;

    @Test
    void createPartner_success() {
        PartnerRequest request = new PartnerRequest("Partner A", BigDecimal.valueOf(100));
        PartnerEntity savedEntity = new PartnerEntity(request.name(), request.creditLimit());

        when(repository.findByName(request.name())).thenReturn(Optional.empty());
        when(repository.saveAndFlush(any(PartnerEntity.class))).thenReturn(savedEntity);

        PartnerResponse response = service.create(request);

        assertNotNull(response);
        assertEquals(request.name(), response.name());
        assertEquals(request.creditLimit(), response.creditLimit());
        verify(repository).findByName(request.name());
        verify(repository).saveAndFlush(any(PartnerEntity.class));
    }

    @Test
    void createPartner_alreadyExists() {
        PartnerRequest request = new PartnerRequest("Partner A", BigDecimal.valueOf(100));
        when(repository.findByName(request.name())).thenReturn(Optional.of(new PartnerEntity(UUID.randomUUID())));

        assertThrows(PartnerAlreadyExistsException.class, () -> service.create(request));
        verify(repository).findByName(request.name());
        verify(repository, never()).saveAndFlush(any());
    }

    @Test
    void getPartner_success() {
        UUID uuid = UUID.randomUUID();
        PartnerEntity entity = new PartnerEntity("Partner A", BigDecimal.valueOf(200));
        entity.setUuid(uuid);

        when(repository.findByUuid(uuid)).thenReturn(Optional.of(entity));

        PartnerResponse response = service.getById(uuid);

        assertNotNull(response);
        assertEquals(uuid, response.uuid());
        assertEquals(entity.getName(), response.name());
        assertEquals(entity.getCreditLimit(), response.creditLimit());
    }

    @Test
    void getPartner_notFound() {
        UUID uuid = UUID.randomUUID();
        when(repository.findByUuid(uuid)).thenReturn(Optional.empty());

        assertThrows(PartnerNotFoundException.class, () -> service.getById(uuid));
    }

    @Test
    void addCredit_success() {
        UUID uuid = UUID.randomUUID();
        PartnerEntity entity = new PartnerEntity("Partner A", BigDecimal.valueOf(100));
        entity.setUuid(uuid);

        when(repository.findByUuid(uuid)).thenReturn(Optional.of(entity));
        when(repository.saveAndFlush(entity)).thenReturn(entity);

        service.addCredit(uuid, new CreditRequest(BigDecimal.valueOf(50)));

        assertEquals(BigDecimal.valueOf(150), entity.getCreditLimit());
        verify(repository).saveAndFlush(entity);
    }

    @Test
    void debitPartner_success() {
        UUID uuid = UUID.randomUUID();
        PartnerEntity entity = new PartnerEntity("Partner A", BigDecimal.valueOf(100));
        entity.setUuid(uuid);

        when(repository.findByUuid(uuid)).thenReturn(Optional.of(entity));
        when(repository.saveAndFlush(entity)).thenReturn(entity);

        service.debitPartner(uuid, new DebitRequest(BigDecimal.valueOf(40)));

        assertEquals(BigDecimal.valueOf(60), entity.getCreditLimit());
        verify(repository).saveAndFlush(entity);
    }

    @Test
    void debitPartner_insufficientCredit() {
        UUID uuid = UUID.randomUUID();
        PartnerEntity entity = new PartnerEntity("Partner A", BigDecimal.valueOf(30));
        entity.setUuid(uuid);

        when(repository.findByUuid(uuid)).thenReturn(Optional.of(entity));

        assertThrows(InsufficientCreditException.class, () ->
                service.debitPartner(uuid, new DebitRequest(BigDecimal.valueOf(50)))
        );

        verify(repository, never()).saveAndFlush(any());
    }

    @Test
    void findPartnerByUuid_notFound_privateMethodCovered() {
        UUID uuid = UUID.randomUUID();
        when(repository.findByUuid(uuid)).thenReturn(Optional.empty());

        assertThrows(PartnerNotFoundException.class, () -> service.getById(uuid));
    }
}
