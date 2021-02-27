package cz.johnczek.dpapi.delivery.service;

import cz.johnczek.dpapi.delivery.dto.DeliveryDto;
import cz.johnczek.dpapi.delivery.entity.DeliveryEntity;
import lombok.NonNull;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface DeliveryService {

    /**
     * @return all possible delivery types
     */
    Collection<DeliveryDto> getAllDeliveryTypes();

    /**
     * @param deliveryId id of delivery we want to find
     * @return entity of delivery, empty optional if delivery could not be found
     */
    Optional<DeliveryEntity> findById(long deliveryId);

    /**
     * @param itemIds item ids for which we want to find linked deliveries
     * @return map where key is delivery id and value is dto of that delivery
     */
    Map<Long, DeliveryDto> findByItemIds(@NonNull Set<Long> itemIds);
}
