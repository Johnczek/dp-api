package cz.johnczek.dpapi.delivery.service;

import cz.johnczek.dpapi.delivery.dto.DeliveryDto;
import cz.johnczek.dpapi.delivery.entity.DeliveryEntity;
import cz.johnczek.dpapi.payment.dto.PaymentDto;
import lombok.NonNull;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface DeliveryService {

    Collection<DeliveryDto> getAllDeliveryTypes();

    Optional<DeliveryEntity> findById(long deliveryId);

    Optional<DeliveryDto> findDtoById(long deliveryId);

    Map<Long, DeliveryDto> findByItemIds(@NonNull Set<Long> itemIds);
}
