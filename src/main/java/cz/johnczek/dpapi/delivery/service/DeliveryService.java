package cz.johnczek.dpapi.delivery.service;

import cz.johnczek.dpapi.delivery.dto.DeliveryDto;
import cz.johnczek.dpapi.delivery.entity.DeliveryEntity;

import java.util.Collection;
import java.util.Optional;

public interface DeliveryService {

    Collection<DeliveryDto> getAllDeliveryTypes();

    Optional<DeliveryEntity> findById(long deliveryId);

    Optional<DeliveryDto> findDtoById(long deliveryId);
}
