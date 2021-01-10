package cz.johnczek.dpapi.user.repository;

import cz.johnczek.dpapi.user.entity.UserEntity;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Query("select u from UserEntity u left join fetch u.roles where u.email = :email")
    Optional<UserEntity> findByEmailWithRolesFetched(@NonNull @Param("email") String email);

    boolean existsByEmail(@NonNull String email);

    Optional<UserEntity> findById(long id);
}
