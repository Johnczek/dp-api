package cz.johnczek.dpapi.user.entity;

import cz.johnczek.dpapi.core.persistence.AbstractIdBasedEntity;
import cz.johnczek.dpapi.file.entity.FileEntity;
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
import java.util.HashSet;
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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserRoleEntity> roles = new HashSet<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "avatar_id")
    private FileEntity avatar;
}
