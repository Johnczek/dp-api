package cz.johnczek.dpapi.user.repository;

import cz.johnczek.dpapi.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
}
