package cz.johnczek.dpapi.user.repository;

import cz.johnczek.dpapi.user.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
}
