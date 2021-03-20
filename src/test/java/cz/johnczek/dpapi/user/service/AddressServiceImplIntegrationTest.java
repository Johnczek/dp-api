package cz.johnczek.dpapi.user.service;

import cz.johnczek.dpapi.AbstractIntegrationTest;
import cz.johnczek.dpapi.core.errorhandling.exception.AddressNotFoundRestException;
import cz.johnczek.dpapi.user.dto.AddressDto;
import cz.johnczek.dpapi.user.entity.AddressEntity;
import cz.johnczek.dpapi.user.entity.UserEntity;
import cz.johnczek.dpapi.user.mapper.AddressMapper;
import cz.johnczek.dpapi.user.repository.AddressRepository;
import cz.johnczek.dpapi.user.repository.UserRepository;
import cz.johnczek.dpapi.user.request.AddressCreationRequest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AddressServiceImplIntegrationTest extends AbstractIntegrationTest {

    private static final String DUMMY_ZIP = "ZIP";
    private static final String DUMMY_STREET = "Streeeeeet";
    private static final String DUMMY_CITY = "City";
    private static final String DUMMY_STREET_NUMBER = "123456";

    @Autowired
    AddressServiceImpl instance;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    AddressMapper addressMapper;

    @Autowired
    UserRepository userRepository;

    @Nested
    class AddAddress {

        @Test
        void validAddressRequest_addressAdded() {

            UserEntity user = userRepository.findByUserId(6L).orElse(null);

            assertThat(user).isNotNull();
            assertThat(user.getAddresses()).isEmpty();

            AddressCreationRequest request = new AddressCreationRequest();
            request.setZipcode(DUMMY_ZIP);
            request.setStreet(DUMMY_STREET);
            request.setStreetNumber(DUMMY_STREET_NUMBER);
            request.setCity(DUMMY_CITY);

            instance.addAddress(user, request);

            List<AddressEntity> userAddresses = addressRepository.findByUserId(6L);
            assertThat(userAddresses).hasSize(1);

            AddressEntity newAddress = userAddresses.get(0);
            assertAll(
                    () -> assertThat(newAddress.getZipcode()).isEqualTo(DUMMY_ZIP),
                    () -> assertThat(newAddress.getStreetNumber()).isEqualTo(DUMMY_STREET_NUMBER),
                    () -> assertThat(newAddress.getStreet()).isEqualTo(DUMMY_STREET),
                    () -> assertThat(newAddress.getCity()).isEqualTo(DUMMY_CITY)
            );

            // cleanup
            addressRepository.delete(newAddress);
            assertThat(addressRepository.findByUserId(6L)).isEmpty();
        }
    }

    @Nested
    class DeleteAddress {

        @Test
        void validAddressIdAndInvalidUserId_exceptionThrown() {

            assertThrows(AddressNotFoundRestException.class,
                    () -> instance.deleteAddress(1L, 1000L)
            );
        }

        @Test
        void invalidAddressIdAndValidUserId_exceptionThrown() {

            assertThrows(AddressNotFoundRestException.class,
                    () -> instance.deleteAddress(1000L, 2L)
            );
        }

        @Test
        void invalidBothParams_exceptionThrown() {

            assertThrows(AddressNotFoundRestException.class,
                    () -> instance.deleteAddress(1000L, 202121L)
            );
        }

        @Test
        void validAddressIdAndUserId_addressDeleted() {

            UserEntity user = userRepository.findByUserId(6L).orElse(null);

            assertThat(user).isNotNull();
            assertThat(user.getAddresses()).isEmpty();

            AddressEntity address = new AddressEntity();
            address.setZipcode(DUMMY_ZIP);
            address.setStreet(DUMMY_STREET);
            address.setStreetNumber(DUMMY_STREET_NUMBER);
            address.setCity(DUMMY_CITY);
            address.setUser(user);
            addressRepository.save(address);

            List<AddressEntity> userAddresses = addressRepository.findByUserId(6L);
            assertThat(userAddresses).hasSize(1);

            instance.deleteAddress(address.getId(), user.getId());

            userAddresses = addressRepository.findByUserId(6L);
            assertThat(userAddresses).isEmpty();
        }
    }

    @Nested
    class FindByUserId {

        @Test
        void nonExistingUser_emptyList() {

            List<AddressDto> result = instance.findByUserId(10000L);

            assertThat(result).isEmpty();
        }

        @Test
        void existingUserWithOneAddress_listWithOneRecord() {

            long userId = 1L;
            List<AddressDto> resultList = instance.findByUserId(userId);

            assertThat(resultList).isNotEmpty().hasSize(1);
            AddressDto firstAddress = resultList.get(0);

            AddressDto expectedResult = addressMapper.entityToDto(addressRepository.findByUserId(userId).get(0));

            assertThat(firstAddress).usingRecursiveComparison().isEqualTo(expectedResult);
        }

    }

    @Nested
    class FindByUserIds {

        @Test
        void noUser_EmptyMap() {

            Map<Long, List<AddressDto>> result = instance.findByUserIds(Collections.emptyList());

            assertThat(result).isEmpty();
        }

        @Test
        void existingUserWithOneAddress_mapWithOneRecord() {

            long userId = 1L;
            Map<Long, List<AddressDto>> result = instance.findByUserIds(Collections.singletonList(userId));

            assertThat(result).isNotEmpty().hasSize(1);
            List<AddressDto> userAddresses = result.get(userId);
            assertThat(userAddresses).isNotEmpty().hasSize(1);

            AddressDto expectedResult = addressMapper.entityToDto(addressRepository.findByUserId(userId).get(0));

            assertThat(userAddresses.get(0)).usingRecursiveComparison().isEqualTo(expectedResult);
        }

        @Test
        void userWithoutAddress_mapWithOneRecordHoldingEmptyList() {

            long userId = 6L;
            Map<Long, List<AddressDto>> result = instance.findByUserIds(Collections.singletonList(userId));

            assertThat(result).isEmpty();
        }

        @Test
        void twoUsersOneWithAddress_mapWithOneRecord() {

            long firstUserId = 1L;
            long secondUserId = 6L;
            Map<Long, List<AddressDto>> result = instance.findByUserIds(List.of(firstUserId, secondUserId));

            assertThat(result).isNotEmpty().hasSize(1);

            List<AddressDto> firstUserAddresses = result.get(firstUserId);
            assertThat(firstUserAddresses).isNotEmpty().hasSize(1);
            AddressDto expectedResult = addressMapper.entityToDto(addressRepository.findByUserId(firstUserId).get(0));
            assertThat(firstUserAddresses.get(0)).usingRecursiveComparison().isEqualTo(expectedResult);
        }
    }
}