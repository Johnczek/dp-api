package cz.johnczek.dpapi.delivery.service;

import com.google.common.collect.Maps;
import cz.johnczek.dpapi.delivery.dto.DeliveryDto;
import cz.johnczek.dpapi.delivery.entity.DeliveryEntity;
import cz.johnczek.dpapi.delivery.mapper.DeliveryMapper;
import cz.johnczek.dpapi.delivery.repository.DeliveryRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryRepository deliveryRepository;

    private final DeliveryMapper deliveryMapper;

    @Override
    @Transactional(readOnly = true)
    public Collection<DeliveryDto> getAllDeliveryTypes() {
        return deliveryRepository.findAll().stream().map(deliveryMapper::entityToDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DeliveryEntity> findById(long deliveryId) {
        return deliveryRepository.findById(deliveryId);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<Long, DeliveryDto> findByItemIds(@NonNull Set<Long> itemIds) {
        if (CollectionUtils.isEmpty(itemIds)) {
            return Collections.emptyMap();
        }

        List<DeliveryDto> deliveriesByIds = deliveryRepository.findByItemIds(itemIds).stream()
                .map(deliveryMapper::entityToDto).collect(Collectors.toList());

        return Maps.uniqueIndex(deliveriesByIds, DeliveryDto::getId);
    }
}
