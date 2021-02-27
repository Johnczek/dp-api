package cz.johnczek.dpapi.user.service;

import cz.johnczek.dpapi.core.errorhandling.exception.AddressNotFoundRestException;
import cz.johnczek.dpapi.user.dto.AddressDto;
import cz.johnczek.dpapi.user.entity.UserEntity;
import cz.johnczek.dpapi.user.request.AddressCreationRequest;
import lombok.NonNull;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface AddressService {

    /**
     * @param user user for which we want to add address
     * @param request request holdign information about address
     * @return data of new address, empty optional in case that we could not add new address
     */
    Optional<AddressDto> addAddress(@NonNull UserEntity user, @NonNull AddressCreationRequest request);

    /**
     * @param addressId id of address to be removed
     * @param userId user owning this address record
     * @throws AddressNotFoundRestException in case that address could not be found
     */
    void deleteAddress(long addressId, long userId);

    /**
     * @param userId id of user for which we want to retrieve addresses
     * @return list of user addresses
     */
    List<AddressDto> findByUserId(long userId);

    /**
     * @param userIds ids of users for which we want to find their addresses
     * @return map where key is user id and value are addresses for given user
     */
    Map<Long, List<AddressDto>> findByUserIds(@NonNull List<Long> userIds);
}
