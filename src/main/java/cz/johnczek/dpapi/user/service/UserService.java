package cz.johnczek.dpapi.user.service;

import cz.johnczek.dpapi.core.errorhandling.exception.FileNotFoundRestException;
import cz.johnczek.dpapi.core.errorhandling.exception.RoleNotFoundRestException;
import cz.johnczek.dpapi.core.errorhandling.exception.UserAlreadyExistsRestException;
import cz.johnczek.dpapi.core.errorhandling.exception.UserNotFoundOrIncorrectPasswordRestException;
import cz.johnczek.dpapi.core.errorhandling.exception.UserNotFoundRestException;
import cz.johnczek.dpapi.user.dto.AddressDto;
import cz.johnczek.dpapi.user.dto.BankAccountDto;
import cz.johnczek.dpapi.user.dto.UserDto;
import cz.johnczek.dpapi.user.entity.UserEntity;
import cz.johnczek.dpapi.user.request.AddressCreationRequest;
import cz.johnczek.dpapi.user.request.BankAccountCreationRequest;
import cz.johnczek.dpapi.user.request.UserChangeAvatarRequest;
import cz.johnczek.dpapi.user.request.UserChangePasswordRequest;
import cz.johnczek.dpapi.user.request.UserChangeRequest;
import cz.johnczek.dpapi.user.request.RegisterRequest;
import cz.johnczek.dpapi.user.response.JwtResponse;
import lombok.NonNull;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface UserService {

    /**
     * @param email email we want to login with
     * @param password password we want to loged with
     * @return response holding jwt token and logged user info
     * @throws UserNotFoundOrIncorrectPasswordRestException in case of bad credentials
     */
    JwtResponse login(@NonNull String email, @NonNull String password);

    /**
     * @return logged user data
     */
    JwtResponse loggedUser();

    /**
     * Method creates new user account.
     *
     * @param registerRequest request holding all user data
     * @throws UserAlreadyExistsRestException in case that user with given identifier already exists
     * @throws RoleNotFoundRestException in case that any of given roles does not exists
     */
    void register(@NonNull RegisterRequest registerRequest);

    /**
     * Method edits user data.
     *
     * @param id id of user we want to edit
     * @param userChangeRequest new user data to be set
     */
    void patch(long id, @NonNull UserChangeRequest userChangeRequest);

    /**
     * @param itemIds ids we want to search sellers
     * @return map where key is item id and value is dto of corresponding seller
     */
    Map<Long, UserDto> findByItemIds(@NonNull Set<Long> itemIds);

    /**
     * Method changes avatar of user.
     *
     * @param id id of user we want to update avatar
     * @param request request holding new avatar identifier
     * @throws FileNotFoundRestException in case that required file could not be retrieved or found in system
     */
    void updateUserAvatar(long id, @NonNull UserChangeAvatarRequest request);

    /**
     * Method changes user password.
     *
     * @param id id of user to update
     * @param request request holding new password
     */
    void updateUserPassword(long id, @NonNull UserChangePasswordRequest request);

    /**
     * Method adds new bank account to user.
     *
     * @param userId id of user we want to add bank account for
     * @param request request holding new bank account data
     * @return data of new bank account, empty optional in case new bank account could not be added
     */
    Optional<BankAccountDto> addBankAccount(long userId, @NonNull BankAccountCreationRequest request);

    /**
     * Method deletes bank account.
     *
     * @param bankAccountId id of bank account to be removed
     * @param userId id of user we want to remove bank account from
     */
    void deleteBankAccount(long bankAccountId, long userId);

    /**
     * Method adds new address to user.
     *
     * @param userId id of user for which we want to add address
     * @param request request holding new address
     * @return data of new address, empty optional in case new address could not be added
     */
    Optional<AddressDto> addAddress(long userId, @NonNull AddressCreationRequest request);

    /**
     * Method deletes address from user account.
     *
     * @param addressId id of address to be deleted
     * @param userId id of user from which we want to delete address
     */
    void deleteAddress(long addressId, long userId);

    /**
     * @param userId id of user to be retrieved
     * @return data of user with given id, empty optional in case that data could not be retrieved
     */
    Optional<UserDto> findById(long userId);

    /**
     * @param userIds ids of users we want to find
     * @return map where key is user id and value is corresponding user information
     */
    Map<Long, UserDto> findByUserIds(@NonNull List<Long> userIds);

    /**
     * @param userId id of user we want to find
     * @return entity of desired user, empty optional in case that user could not be retrieved
     * @throws UserNotFoundRestException in case that user is not present in system
     */
    Optional<UserEntity> findEntityById(long userId);

    Optional<UserEntity> findUserByJwtToken(@NonNull String userJwtToken);
}
