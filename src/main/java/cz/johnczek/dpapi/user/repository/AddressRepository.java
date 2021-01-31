package cz.johnczek.dpapi.user.repository;

import cz.johnczek.dpapi.user.entity.AddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AddressRepository extends JpaRepository<AddressEntity, Long> {

    @Query("select ad from AddressEntity ad where ad.id = :addressId and ad.user.id = :userId")
    Optional<AddressEntity> findByIdAndUserId(@Param("addressId") long addressId, @Param("userId") long userId);

    @Query("select a from AddressEntity a where a.user.id = :userId")
    List<AddressEntity> findByUserId(@Param("userId") long userId);
}
