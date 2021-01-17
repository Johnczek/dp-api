package cz.johnczek.dpapi.payment.entity;

import cz.johnczek.dpapi.core.persistence.AbstractIdBasedEntity;
import cz.johnczek.dpapi.file.entity.FileEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;

@Setter
@Getter
@Entity
@NoArgsConstructor
@Table(name = "payment", schema = "public")
public class PaymentEntity extends AbstractIdBasedEntity<Long> {

    private String name;

    private BigDecimal price;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "logo_id")
    private FileEntity logo;
}
