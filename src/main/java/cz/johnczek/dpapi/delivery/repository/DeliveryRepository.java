package cz.johnczek.dpapi.delivery.repository;

import cz.johnczek.dpapi.delivery.entity.DeliveryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DeliveryRepository extends JpaRepository<DeliveryEntity, Long> {

    List<DeliveryEntity> findAll();

    Optional<DeliveryEntity> findById(long id);
}
