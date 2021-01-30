package cz.johnczek.dpapi.user.service;

import cz.johnczek.dpapi.user.entity.UserEntity;
import cz.johnczek.dpapi.user.request.AddressCreationRequest;
import lombok.NonNull;

public interface AddressService {

    void addAddress(@NonNull UserEntity user, @NonNull AddressCreationRequest request);

    void deleteAddress(long addressId, long userId);
}
