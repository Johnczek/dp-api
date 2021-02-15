package cz.johnczek.dpapi.user.service;

import cz.johnczek.dpapi.core.errorhandling.exception.AddressNotFoundRestException;
import cz.johnczek.dpapi.user.dto.AddressDto;
import cz.johnczek.dpapi.user.entity.AddressEntity;
import cz.johnczek.dpapi.user.entity.UserEntity;
import cz.johnczek.dpapi.user.mapper.AddressMapper;
import cz.johnczek.dpapi.user.repository.AddressRepository;
import cz.johnczek.dpapi.user.request.AddressCreationRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;

    private final AddressMapper addressMapper;

    @Override
    @Transactional
    public Optional<AddressDto> addAddress(@NonNull UserEntity user, @NonNull AddressCreationRequest request) {

        AddressEntity address = addressMapper.requestToEntity(request);
        address.setUser(user);
        return Optional.ofNullable(addressMapper.entityToDto(addressRepository.save(address)));
    }

    @Override
    @Transactional
    public void deleteAddress(long addressId, long userId) {

        AddressEntity addressEntity = addressRepository.findByIdAndUserId(addressId, userId).orElseThrow(() -> {
            log.warn("Address deletion failed. Address with id {} for user with id {} not found in database", addressId, userId);

            return new AddressNotFoundRestException(addressId);
        });

        addressRepository.delete(addressEntity);

    }

    @Override
    public List<AddressDto> findByUserId(long userId) {
        return addressRepository.findByUserId(userId).stream()
                .map(addressMapper::entityToDto)
                .collect(Collectors.toList());
    }
}
