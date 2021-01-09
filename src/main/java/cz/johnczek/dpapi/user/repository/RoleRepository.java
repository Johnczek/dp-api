package cz.johnczek.dpapi.user.repository;

import cz.johnczek.dpapi.user.entity.RoleEntity;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

    Optional<RoleEntity> findByCode(@NonNull String code);
}
