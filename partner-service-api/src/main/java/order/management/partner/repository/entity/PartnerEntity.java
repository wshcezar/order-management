package order.management.partner.repository.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "partner")
public class PartnerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private UUID uuid;
    private String name;
    private BigDecimal creditLimit;

    protected PartnerEntity() {}

    public PartnerEntity(String name, BigDecimal creditLimit) {
        this.name = name;
        this.creditLimit = creditLimit;
    }

    public PartnerEntity(UUID uuid) {
        this.uuid = uuid;
    }

    @PrePersist
    void init() {
         this.uuid = UUID.randomUUID();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PartnerEntity entity = (PartnerEntity) o;
        return Objects.equals(id, entity.id) && Objects.equals(uuid, entity.uuid) && Objects.equals(name, entity.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, uuid, name);
    }
}
