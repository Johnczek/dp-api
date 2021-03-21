package cz.johnczek.dpapi.user.service;

import cz.johnczek.dpapi.AbstractIntegrationTest;
import cz.johnczek.dpapi.core.errorhandling.exception.AddressNotFoundRestException;
import cz.johnczek.dpapi.core.errorhandling.exception.BankAccountNotFoundRestException;
import cz.johnczek.dpapi.core.errorhandling.exception.BaseForbiddenRestException;
import cz.johnczek.dpapi.core.errorhandling.exception.FileNotFoundRestException;
import cz.johnczek.dpapi.core.errorhandling.exception.UserAlreadyExistsRestException;
import cz.johnczek.dpapi.core.errorhandling.exception.UserNotFoundOrIncorrectPasswordRestException;
import cz.johnczek.dpapi.core.errorhandling.exception.UserNotFoundRestException;
import cz.johnczek.dpapi.user.dto.UserDto;
import cz.johnczek.dpapi.user.entity.UserEntity;
import cz.johnczek.dpapi.user.repository.UserRepository;
import cz.johnczek.dpapi.user.request.AddressCreationRequest;
import cz.johnczek.dpapi.user.request.BankAccountCreationRequest;
import cz.johnczek.dpapi.user.request.RegisterRequest;
import cz.johnczek.dpapi.user.request.UserChangeAvatarRequest;
import cz.johnczek.dpapi.user.request.UserChangePasswordRequest;
import cz.johnczek.dpapi.user.request.UserChangeRequest;
import cz.johnczek.dpapi.user.response.JwtResponse;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserServiceImplIntegrationTest extends AbstractIntegrationTest {

    private static final String DUMMY_DESCRIPTION = "DESC";
    private static final String DUMMY_EMAIL = "dummy@email.com";
    private static final String DUMMY_FIRST_NAME = "DFirstName";
    private static final String DUMMY_LAST_NAME = "DFirstName";
    private static final String DUMMY_PASSWORD = "pw";
    private static final int DUMMY_BANK_CODE = 1010;
    private static final long DUMMY_PREFIX = 1000;
    private static final long DUMMY_NUMBER = 123456789;
    private static final String DUMMY_CITY = "City";
    private static final String DUMMY_STREET = "Street";
    private static final String DUMMY_STREET_NUMBER = "StreetNr";
    private static final String DUMMY_ZIPCODE = "12300";

    @Autowired
    private UserServiceImpl instance;

    @Autowired
    private UserRepository userRepository;

    @Nested
    class Login {

        @Test
        void badCredentials_exceptionThrown() {
            assertThrows(UserNotFoundOrIncorrectPasswordRestException.class,
                    () -> instance.login("invalid_email@email.com", "invalidPW")
            );
        }

        @Test
        void correctCredentials_userLoggedResponse() {

            String userEmail = "user@user.com";
            String userPassword = "user";
            UserEntity userEntity = userRepository.findByUserEmail(userEmail).orElse(null);

            assertThat(userEntity).isNotNull();

            JwtResponse loggedUser = instance.login(userEmail, userPassword);

            assertThat(loggedUser).isNotNull();

            assertAll(
                    () -> assertThat(loggedUser.getId()).isEqualTo(userEntity.getId()),
                    () -> assertThat(loggedUser.getEmail()).isEqualTo(userEmail),
                    () -> assertThat(loggedUser.getFirstName()).isEqualTo(userEntity.getFirstName()),
                    () -> assertThat(loggedUser.getLastName()).isEqualTo(userEntity.getLastName()),
                    () -> assertThat(loggedUser.getRoles()).hasSize(1),
                    () -> assertThat(loggedUser.getToken()).isNotBlank()
            );
        }
    }

    @Nested
    class LoggedUser {

        @Test
        void notLoggedUser_exceptionThrown() {
            assertThrows(BaseForbiddenRestException.class,
                    () -> instance.loggedUser()
            );
        }

        @Test
        void loggedUser_userDataReturned() {

            String userEmail = "user@user.com";
            String userPassword = "user";
            JwtResponse loggedUser = instance.login(userEmail, userPassword);

            JwtResponse result = instance.loggedUser();

            assertThat(loggedUser)
                    .usingRecursiveComparison()
                    .isEqualTo(result);
        }
    }

    @Nested
    class Register {

        @Test
        void userAlreadyExists_exceptionThrown() {

            RegisterRequest request = new RegisterRequest();
            request.setEmail("user@user.com");

            assertThrows(UserAlreadyExistsRestException.class,
                    () -> instance.register(request)
            );
        }

        @Test
        void validRequest_userCreated() {

            RegisterRequest request = new RegisterRequest();
            request.setDescription(DUMMY_DESCRIPTION);
            request.setEmail(DUMMY_EMAIL);
            request.setFirstName(DUMMY_FIRST_NAME);
            request.setLastName(DUMMY_LAST_NAME);
            request.setPassword(DUMMY_PASSWORD);

            instance.register(request);

            UserEntity newUser = userRepository.findByUserEmail(DUMMY_EMAIL).orElse(null);

            assertThat(newUser).isNotNull();

            assertAll(
                    () -> assertThat(newUser.getEmail()).isEqualTo(DUMMY_EMAIL),
                    () -> assertThat(newUser.getFirstName()).isEqualTo(DUMMY_FIRST_NAME),
                    () -> assertThat(newUser.getLastName()).isEqualTo(DUMMY_LAST_NAME),
                    () -> assertThat(newUser.getRoles()).hasSize(1),
                    () -> assertThat(newUser.getPassword()).isNotBlank()
            );

            // cleanup
            userRepository.delete(newUser);
        }
    }

    @Nested
    class Patch {

        @Test
        void notLoggedUser_exceptionThrown() {

            UserChangeRequest request = new UserChangeRequest();
            long userId = 100L;

            assertThrows(BaseForbiddenRestException.class,
                    () -> instance.patch(userId, request)
            );
        }

        @Test
        void userNotFound_exceptionThrown() {

            String userEmail = "user@user.com";
            String userPassword = "user";
            instance.login(userEmail, userPassword);

            UserChangeRequest request = new UserChangeRequest();
            long userId = 100L;

            assertThrows(UserNotFoundRestException.class,
                    () -> instance.patch(userId, request)
            );
        }

        @Test
        void updatedUserNotMatchWithLoggedUser_exceptionThrown() {

            String userEmail = "user@user.com";
            String userPassword = "user";
            instance.login(userEmail, userPassword);

            UserChangeRequest request = new UserChangeRequest();
            long userId = 2L;

            assertThrows(BaseForbiddenRestException.class,
                    () -> instance.patch(userId, request)
            );
        }

        @Test
        void validUserAndValidData_userDataUpdated() {

            String userEmail = "user@user.com";
            String userPassword = "user";

            UserEntity originalUser = userRepository.findByUserEmail(userEmail).orElse(null);
            assertThat(originalUser).isNotNull();

            instance.login(userEmail, userPassword);

            UserChangeRequest request = new UserChangeRequest();
            request.setFirstName(DUMMY_FIRST_NAME);
            request.setLastName(DUMMY_LAST_NAME);
            request.setDescription(DUMMY_DESCRIPTION);

            instance.patch(originalUser.getId(), request);
            UserEntity updatedUser = userRepository.findByUserEmail(userEmail).orElse(null);
            assertThat(updatedUser).isNotNull();

            assertAll(
                    () -> assertThat(updatedUser.getFirstName()).isEqualTo(DUMMY_FIRST_NAME),
                    () -> assertThat(updatedUser.getLastName()).isEqualTo(DUMMY_LAST_NAME),
                    () -> assertThat(updatedUser.getDescription()).isEqualTo(DUMMY_DESCRIPTION)
            );

            // cleanup
            userRepository.save(originalUser);
        }
    }

    @Nested
    class FindByItemIds {

        @Test
        void noItemIds_emptyMap() {

            Map<Long, UserDto> result = instance.findByItemIds(Collections.emptySet());

            assertThat(result).isEmpty();
        }

        @Test
        void invalidItemId_emptyMap() {

            Map<Long, UserDto> result = instance.findByItemIds(Set.of(10000L));

            assertThat(result).isEmpty();
        }

        @Test
        void validItemId_mapWithOneRecord() {
            long itemId = 1L;

            Map<Long, UserDto> resultMap = instance.findByItemIds(Set.of(itemId));

            assertThat(resultMap).hasSize(1);

            UserDto result = resultMap.get(itemId);
            assertThat(result).isNotNull();
            assertThat(result.getEmail()).isEqualTo("user@user.com");
        }

        @Test
        void oneValidAndOneInvalidItemId_mapWithOneRecord() {
            long itemId = 1L;

            Map<Long, UserDto> resultMap = instance.findByItemIds(Set.of(itemId, 10000L));

            assertThat(resultMap).hasSize(1);

            UserDto result = resultMap.get(itemId);
            assertThat(result).isNotNull();
            assertThat(result.getEmail()).isEqualTo("user@user.com");
        }
    }

    @Nested
    class UpdateUserAvatar {

        @Test
        void notExistingFileEntity_exceptionThrown() {

            UserChangeAvatarRequest request = new UserChangeAvatarRequest();
            request.setAvatarUUID("false UUID");
            long userId = 100L;

            assertThrows(FileNotFoundRestException.class,
                    () -> instance.updateUserAvatar(userId, request)
            );
        }


        @Test
        void notLoggedUser_exceptionThrown() {

            UserChangeAvatarRequest request = new UserChangeAvatarRequest();
            request.setAvatarUUID("3cedb358-58de-11eb-ae93-0242ac130002");
            long userId = 100L;

            assertThrows(BaseForbiddenRestException.class,
                    () -> instance.updateUserAvatar(userId, request)
            );
        }

        @Test
        void userNotFound_exceptionThrown() {

            String userEmail = "user@user.com";
            String userPassword = "user";
            instance.login(userEmail, userPassword);

            UserChangeAvatarRequest request = new UserChangeAvatarRequest();
            request.setAvatarUUID("3cedb358-58de-11eb-ae93-0242ac130002");
            long userId = 100L;

            assertThrows(UserNotFoundRestException.class,
                    () -> instance.updateUserAvatar(userId, request)
            );
        }

        @Test
        void userNotMatchWithLoggedUser_exceptionThrown() {

            String userEmail = "user@user.com";
            String userPassword = "user";
            instance.login(userEmail, userPassword);

            UserChangeAvatarRequest request = new UserChangeAvatarRequest();
            request.setAvatarUUID("3cedb358-58de-11eb-ae93-0242ac130002");
            long userId = 2L;

            assertThrows(BaseForbiddenRestException.class,
                    () -> instance.updateUserAvatar(userId, request)
            );
        }

        @Test
        void validData_avatarUpdated() {

            String userEmail = "user@user.com";
            String userPassword = "user";
            instance.login(userEmail, userPassword);

            String newFileIdentifier = "3cedb358-58de-11eb-ae93-0242ac130002";
            UserChangeAvatarRequest request = new UserChangeAvatarRequest();
            request.setAvatarUUID(newFileIdentifier);
            long userId = 1L;

            UserEntity originalUser = userRepository.findByUserEmail("user@user.com").orElse(null);
            assertThat(originalUser).isNotNull();

            instance.updateUserAvatar(userId, request);

            UserEntity updatedUser = userRepository.findByUserEmail("user@user.com").orElse(null);
            assertThat(updatedUser).isNotNull();
            assertThat(updatedUser.getAvatar()).isNotNull();
            assertThat(updatedUser.getAvatar().getFileIdentifier()).isEqualTo(newFileIdentifier);

            // cleanup
            userRepository.save(originalUser);
        }
    }

    @Nested
    class UpdateUserPassword {

        @Test
        void notLoggedUser_exceptionThrown() {

            UserChangePasswordRequest request = new UserChangePasswordRequest();
            request.setPassword("newPw");
            long userId = 100L;

            assertThrows(BaseForbiddenRestException.class,
                    () -> instance.updateUserPassword(userId, request)
            );
        }

        @Test
        void userNotFound_exceptionThrown() {

            String userEmail = "user@user.com";
            String userPassword = "user";
            instance.login(userEmail, userPassword);

            UserChangePasswordRequest request = new UserChangePasswordRequest();
            request.setPassword("newPw");
            long userId = 100L;

            assertThrows(UserNotFoundRestException.class,
                    () -> instance.updateUserPassword(userId, request)
            );
        }

        @Test
        void loggedUserNotCorrespondToUpdatedUser_exceptionThrown() {

            String userEmail = "user@user.com";
            String userPassword = "user";
            instance.login(userEmail, userPassword);

            UserChangePasswordRequest request = new UserChangePasswordRequest();
            request.setPassword("newPw");
            long userId = 3L;

            assertThrows(BaseForbiddenRestException.class,
                    () -> instance.updateUserPassword(userId, request)
            );
        }

        @Test
        void validData_passwordChanged() {

            String userEmail = "user@user.com";
            String userPassword = "user";
            instance.login(userEmail, userPassword);

            UserChangePasswordRequest request = new UserChangePasswordRequest();
            request.setPassword("newPw");
            long userId = 1L;

            UserEntity originalUser = userRepository.findByUserEmail("user@user.com").orElse(null);
            assertThat(originalUser).isNotNull();
            String originalPw = originalUser.getPassword();

            instance.updateUserPassword(userId, request);

            UserEntity updatedUser = userRepository.findByUserEmail("user@user.com").orElse(null);
            assertThat(updatedUser).isNotNull();
            assertThat(updatedUser.getPassword()).isNotEqualTo(originalPw);

            // cleanup
            userRepository.save(originalUser);
        }
    }

    @Nested
    class AddBankAccount {

        @Test
        void notLoggedUser_exceptionThrown() {

            BankAccountCreationRequest request = new BankAccountCreationRequest();
            long userId = 100L;

            assertThrows(BaseForbiddenRestException.class,
                    () -> instance.addBankAccount(userId, request)
            );
        }

        @Test
        void userNotFound_exceptionThrown() {

            String userEmail = "user6WithoutBankAccount@user.com";
            String userPassword = "user";
            instance.login(userEmail, userPassword);

            BankAccountCreationRequest request = new BankAccountCreationRequest();
            long userId = 100L;

            assertThrows(UserNotFoundRestException.class,
                    () -> instance.addBankAccount(userId, request)
            );
        }

        @Test
        void loggedUserDoesNotCorrespondToUpdatedUser_exceptionThrown() {

            String userEmail = "user6WithoutBankAccount@user.com";
            String userPassword = "user";
            instance.login(userEmail, userPassword);

            BankAccountCreationRequest request = new BankAccountCreationRequest();
            long userId = 2L;

            assertThrows(BaseForbiddenRestException.class,
                    () -> instance.addBankAccount(userId, request)
            );
        }

        @Test
        void validData_bankAccountAdded() {

            String userEmail = "user6WithoutBankAccount@user.com";
            String userPassword = "user";
            instance.login(userEmail, userPassword);

            BankAccountCreationRequest request = new BankAccountCreationRequest();
            request.setPrefix(DUMMY_PREFIX);
            request.setNumber(DUMMY_NUMBER);
            request.setBankCode(DUMMY_BANK_CODE);
            long userId = 7L;

            UserEntity originalUser = userRepository.findByUserEmail(userEmail).orElse(null);
            assertThat(originalUser).isNotNull();

            assertThat(originalUser.getBankAccounts()).isEmpty();

            instance.addBankAccount(userId, request);

            assertAll(
                    () -> assertThat(originalUser.getBankAccounts()).isNotEmpty(),
                    () -> assertThat(originalUser.getBankAccounts().get(0).getPrefix()).isEqualTo(DUMMY_PREFIX),
                    () -> assertThat(originalUser.getBankAccounts().get(0).getBankCode()).isEqualTo(DUMMY_BANK_CODE),
                    () -> assertThat(originalUser.getBankAccounts().get(0).getNumber()).isEqualTo(DUMMY_NUMBER)
            );

            // cleanup
            originalUser.setBankAccounts(Collections.emptyList());
            userRepository.save(originalUser);
        }
    }

    @Nested
    class DeleteBankAccount {

        @Test
        void notLoggedUser_exceptionThrown() {

            long userId = 100L;
            long bankAccountId = 100L;

            assertThrows(BaseForbiddenRestException.class,
                    () -> instance.deleteBankAccount(bankAccountId, userId)
            );
        }

        @Test
        void userNotFound_exceptionThrown() {

            String userEmail = "user6WithoutBankAccount@user.com";
            String userPassword = "user";
            instance.login(userEmail, userPassword);

            long userId = 100L;
            long bankAccountId = 100L;

            assertThrows(UserNotFoundRestException.class,
                    () -> instance.deleteBankAccount(bankAccountId, userId)
            );
        }

        @Test
        void loggedUserDoesNotCorrespondToUpdatedUser_exceptionThrown() {

            String userEmail = "user6WithoutBankAccount@user.com";
            String userPassword = "user";
            instance.login(userEmail, userPassword);

            long userId = 2L;
            long bankAccountId = 100L;

            assertThrows(BaseForbiddenRestException.class,
                    () -> instance.deleteBankAccount(bankAccountId, userId)
            );
        }

        @Test
        void bankAccountNotFound_exceptionThrown() {

            String userEmail = "user6WithoutBankAccount@user.com";
            String userPassword = "user";
            instance.login(userEmail, userPassword);

            long userId = 7L;
            long bankAccountId = 100L;

            assertThrows(BankAccountNotFoundRestException.class,
                    () -> instance.deleteBankAccount(bankAccountId, userId)
            );
        }
    }

    @Nested
    class AddAddress {

        @Test
        void notLoggedUser_exceptionThrown() {

            AddressCreationRequest request = new AddressCreationRequest();
            long userId = 100L;

            assertThrows(BaseForbiddenRestException.class,
                    () -> instance.addAddress(userId, request)
            );
        }

        @Test
        void userNotFound_exceptionThrown() {

            String userEmail = "user5WithoutAddress@user.com";
            String userPassword = "user";
            instance.login(userEmail, userPassword);

            AddressCreationRequest request = new AddressCreationRequest();
            long userId = 100L;

            assertThrows(UserNotFoundRestException.class,
                    () -> instance.addAddress(userId, request)
            );
        }

        @Test
        void loggedUserDoesNotCorrespondToUpdatedUser_exceptionThrown() {

            String userEmail = "user5WithoutAddress@user.com";
            String userPassword = "user";
            instance.login(userEmail, userPassword);

            AddressCreationRequest request = new AddressCreationRequest();
            long userId = 2L;

            assertThrows(BaseForbiddenRestException.class,
                    () -> instance.addAddress(userId, request)
            );
        }

        @Test
        void validData_addressAdded() {

            String userEmail = "user5WithoutAddress@user.com";
            String userPassword = "user";
            instance.login(userEmail, userPassword);

            AddressCreationRequest request = new AddressCreationRequest();
            request.setStreet(DUMMY_STREET);
            request.setStreetNumber(DUMMY_STREET_NUMBER);
            request.setCity(DUMMY_CITY);
            request.setZipcode(DUMMY_ZIPCODE);
            long userId = 6L;

            UserEntity originalUser = userRepository.findByUserEmail(userEmail).orElse(null);
            assertThat(originalUser).isNotNull();

            assertThat(originalUser.getAddresses()).isEmpty();

            instance.addAddress(userId, request);

            assertAll(
                    () -> assertThat(originalUser.getAddresses()).isNotEmpty(),
                    () -> assertThat(originalUser.getAddresses().get(0).getStreet()).isEqualTo(DUMMY_STREET),
                    () -> assertThat(originalUser.getAddresses().get(0).getStreetNumber()).isEqualTo(DUMMY_STREET_NUMBER),
                    () -> assertThat(originalUser.getAddresses().get(0).getCity()).isEqualTo(DUMMY_CITY),
                    () -> assertThat(originalUser.getAddresses().get(0).getZipcode()).isEqualTo(DUMMY_ZIPCODE)
            );

            // cleanup
            originalUser.setAddresses(Collections.emptyList());
            userRepository.save(originalUser);
        }
    }

    @Nested
    class DeleteAddress {

        @Test
        void notLoggedUser_exceptionThrown() {

            long userId = 100L;
            long addressId = 100L;

            assertThrows(BaseForbiddenRestException.class,
                    () -> instance.deleteAddress(addressId, userId)
            );
        }

        @Test
        void userNotFound_exceptionThrown() {

            String userEmail = "user5WithoutAddress@user.com";
            String userPassword = "user";
            instance.login(userEmail, userPassword);

            long userId = 100L;
            long addressId = 100L;

            assertThrows(UserNotFoundRestException.class,
                    () -> instance.deleteAddress(addressId, userId)
            );
        }

        @Test
        void loggedUserDoesNotCorrespondToUpdatedUser_exceptionThrown() {

            String userEmail = "user5WithoutAddress@user.com";
            String userPassword = "user";
            instance.login(userEmail, userPassword);

            long userId = 2L;
            long addressId = 100L;

            assertThrows(BaseForbiddenRestException.class,
                    () -> instance.deleteAddress(addressId, userId)
            );
        }

        @Test
        void addressNotFound_exceptionThrown() {

            String userEmail = "user5WithoutAddress@user.com";
            String userPassword = "user";
            instance.login(userEmail, userPassword);

            long userId = 6L;
            long addressId = 100L;

            assertThrows(AddressNotFoundRestException.class,
                    () -> instance.deleteAddress(addressId, userId)
            );
        }
    }

    @Nested
    class FindById {

        @Test
        void userNotFound_exceptionThrown() {

            long userId = 2000L;

            assertThrows(UserNotFoundRestException.class,
                    () -> instance.findById(userId)
            );
        }

        @Test
        void userFound_dtoReturned() {

            long userId = 1L;

            UserDto result = instance.findById(userId).orElse(null);

            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(userId);
        }
    }

    @Nested
    class FindByUserIds {

        @Test
        void noInput_emptyMap() {

            Map<Long, UserDto> result = instance.findByUserIds(Collections.emptyList());

            assertThat(result).isEmpty();
        }

        @Test
        void oneInvalidUserId_emptyMap() {

            long userId = 10000L;

            Map<Long, UserDto> result = instance.findByUserIds(List.of(userId));

            assertThat(result).isEmpty();
        }

        @Test
        void oneValidUserId_mapWithOneItem() {

            long userId = 1L;

            Map<Long, UserDto> result = instance.findByUserIds(List.of(userId));

            assertThat(result).hasSize(1);
            assertThat(result.get(userId)).isNotNull();
            assertThat(result.get(userId).getId()).isEqualTo(userId);
        }

        @Test
        void oneValidAndOneInvalidUserId_mapWithOneItem() {

            long validUserId = 1L;
            long invalidUserId = 6666L;

            Map<Long, UserDto> result = instance.findByUserIds(List.of(validUserId, invalidUserId));

            assertThat(result).hasSize(1);
            assertThat(result.get(validUserId)).isNotNull();
            assertThat(result.get(validUserId).getId()).isEqualTo(validUserId);
        }
    }

    @Nested
    class FindEntityById {

        @Test
        void userNotFound_exceptionThrown() {

            long userId = 2000L;

            assertThrows(UserNotFoundRestException.class,
                    () -> instance.findEntityById(userId)
            );
        }

        @Test
        void userFound_dtoReturned() {

            long userId = 1L;

            UserEntity result = instance.findEntityById(userId).orElse(null);

            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(userId);
        }
    }
}
