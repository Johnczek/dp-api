package cz.johnczek.dpapi.user.repository;

import cz.johnczek.dpapi.user.entity.UserEntity;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Query("select u " +
            "from UserEntity u " +
            "left join fetch u.roles " +
            "left join fetch u.avatar " +
            "where u.email = :email")
    Optional<UserEntity> findByEmailWithRolesAndAvatarFetched(@NonNull @Param("email") String email);

    @Query("select s " +
            "from ItemEntity i " +
            "left join i.seller s " +
            "left join fetch s.avatar a " +
            "where i.id in (:itemIds)")
    Set<UserEntity> findByItemIdsWithAvatarFetched(@Param("itemIds") Set<Long> itemIds);

    boolean existsByEmail(String email);

    @Query("select u from UserEntity u where u.id = :userId")
    Optional<UserEntity> findByUserId(@Param("userId") long userId);

    @Query("select u from UserEntity u where u.email = :email")
    Optional<UserEntity> findByUserEmail(@Param("email") @NonNull String email);

    @Query("select u from UserEntity u where u.id in (:userId)")
    List<UserEntity> findByUserIds(@Param("userId") @NonNull List<Long> userIds);
}
