package cz.johnczek.dpapi.delivery.service;

import cz.johnczek.dpapi.delivery.dto.DeliveryDto;
import cz.johnczek.dpapi.delivery.entity.DeliveryEntity;
import cz.johnczek.dpapi.delivery.mapper.DeliveryMapper;
import cz.johnczek.dpapi.delivery.repository.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryRepository deliveryRepository;

    private final DeliveryMapper deliveryMapper;

    @Override
    public Collection<DeliveryDto> getAllDeliveryTypes() {
        return deliveryRepository.findAll().stream().map(deliveryMapper::entityToDto).collect(Collectors.toList());
    }

    @Override
    public Optional<DeliveryEntity> findById(long deliveryId) {
        return deliveryRepository.findById(deliveryId);
    }

    @Override
    public Optional<DeliveryDto> findDtoById(long deliveryId) {
        return deliveryRepository.findById(deliveryId).map(deliveryMapper::entityToDto);
    }
}
