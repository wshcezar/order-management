package order.management.partner.repository;

import order.management.partner.repository.entity.PartnerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PartnerRepository extends JpaRepository<PartnerEntity, Long> {

    Optional<PartnerEntity> findByUuid(final UUID uuid);

    Optional<PartnerEntity> findByName(final String name);
}
