package cz.johnczek.dpapi.user.entity;

import cz.johnczek.dpapi.core.persistence.AbstractIdBasedEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "address", schema = "public")
public class AddressEntity extends AbstractIdBasedEntity<Long> {

    private String street;

    private String streetNumber;

    private String zipcode;

    private String city;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity user;
}
