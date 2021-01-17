package cz.johnczek.dpapi.item.entity;

import cz.johnczek.dpapi.core.persistence.AbstractIdBasedEntity;
import cz.johnczek.dpapi.user.entity.RoleEntity;
import cz.johnczek.dpapi.user.entity.UserEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@NoArgsConstructor
@Table(name = "item_bid", schema = "public")
public class ItemBidEntity extends AbstractIdBasedEntity<Long> {

    @JoinColumn(name = "buyer_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity buyer;

    @JoinColumn(name = "item_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private ItemEntity item;

    private BigDecimal amount;

    private LocalDateTime time;
}
