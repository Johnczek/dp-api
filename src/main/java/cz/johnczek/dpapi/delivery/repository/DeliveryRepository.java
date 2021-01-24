package cz.johnczek.dpapi.delivery.repository;

import cz.johnczek.dpapi.delivery.entity.DeliveryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface DeliveryRepository extends JpaRepository<DeliveryEntity, Long> {

    @Query("select d " +
            "from DeliveryEntity d " +
            "left join fetch d.logo")
    List<DeliveryEntity> findAll();

    @Query("select d " +
            "from DeliveryEntity d " +
            "left join fetch d.logo " +
            "where d.id = :id")
    Optional<DeliveryEntity> findById(long id);

    @Query("select d " +
            "from ItemEntity i " +
            "left join i.delivery d " +
            "left join fetch d.logo " +
            "where i.id in (:itemIds)")
    Set<DeliveryEntity> findByItemIds(@Param("itemIds") Set<Long> itemIds);
}
