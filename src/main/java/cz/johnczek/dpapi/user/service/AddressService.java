package cz.johnczek.dpapi.user.service;

import cz.johnczek.dpapi.user.dto.AddressDto;
import cz.johnczek.dpapi.user.entity.UserEntity;
import cz.johnczek.dpapi.user.request.AddressCreationRequest;
import lombok.NonNull;

import java.util.List;
import java.util.Optional;

public interface AddressService {

    Optional<AddressDto> addAddress(@NonNull UserEntity user, @NonNull AddressCreationRequest request);

    void deleteAddress(long addressId, long userId);

    List<AddressDto> findByUserId(long userId);
}
