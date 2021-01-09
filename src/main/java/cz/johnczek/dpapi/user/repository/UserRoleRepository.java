package cz.johnczek.dpapi.user.repository;

import cz.johnczek.dpapi.user.entity.UserRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRoleEntity, Long> {
}
