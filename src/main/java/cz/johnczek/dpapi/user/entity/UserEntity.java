package cz.johnczek.dpapi.user.entity;

import cz.johnczek.dpapi.core.persistence.AbstractIdBasedEntity;
import cz.johnczek.dpapi.file.entity.FileEntity;
import cz.johnczek.dpapi.item.entity.ItemBidEntity;
import cz.johnczek.dpapi.order.entity.OrderEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "user", schema = "public")
public class UserEntity extends AbstractIdBasedEntity<Long> {

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private String description;

    private LocalDateTime registered;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "avatar_id")
    private FileEntity avatar;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserRoleEntity> roles = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "buyer", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ItemBidEntity> bids = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BankAccountEntity> bankAccounts = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AddressEntity> addresses = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "buyer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderEntity> boughtOrders = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "seller", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderEntity> soldOrders = new ArrayList<>();
}
