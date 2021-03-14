package cz.johnczek.dpapi.user.mapper;

import cz.johnczek.dpapi.user.dto.AddressDto;
import cz.johnczek.dpapi.user.entity.AddressEntity;
import cz.johnczek.dpapi.user.entity.UserEntity;
import cz.johnczek.dpapi.user.request.AddressCreationRequest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@ExtendWith(MockitoExtension.class)
class AddressMapperUnitTest {

    private static final long DUMMY_ADDRESS_ID = 10L;
    private static final String DUMMY_CITY = "City";
    private static final String DUMMY_STREET = "Street";
    private static final String DUMMY_STREET_NUMBER = "StreetNr";
    private static final String DUMMY_ZIPCODE = "12300";
    private static final long DUMMY_USER_ID = 1L;

    @InjectMocks
    private AddressMapperImpl instance;

    @Nested
    class RequestToEntity {

        @Test
        void nullInput_nullOutput() {

            AddressEntity result = instance.requestToEntity(null);

            assertThat(result).isNull();
        }

        @Test
        void validInput_validEntity() {

            AddressEntity result = instance.requestToEntity(prepareDummyRequest());

            assertAll(
                    () -> assertThat(result).isNotNull(),
                    () -> assertThat(result.getCity()).isEqualTo(DUMMY_CITY),
                    () -> assertThat(result.getStreet()).isEqualTo(DUMMY_STREET),
                    () -> assertThat(result.getStreetNumber()).isEqualTo(DUMMY_STREET_NUMBER),
                    () -> assertThat(result.getZipcode()).isEqualTo(DUMMY_ZIPCODE)
            );
        }

        private AddressCreationRequest prepareDummyRequest() {

            AddressCreationRequest request = new AddressCreationRequest();
            request.setCity(DUMMY_CITY);
            request.setStreet(DUMMY_STREET);
            request.setStreetNumber(DUMMY_STREET_NUMBER);
            request.setZipcode(DUMMY_ZIPCODE);

            return request;
        }
    }

    @Nested
    class EntityToDto {

        @Test
        void nullInput_nullOutput() {

            AddressDto result = instance.entityToDto(null);

            assertThat(result).isNull();
        }

        @Test
        void validInput_validEntity() {

            AddressDto result = instance.entityToDto(prepareAddressEntity());

            assertAll(
                    () -> assertThat(result).isNotNull(),
                    () -> assertThat(result.getId()).isEqualTo(DUMMY_ADDRESS_ID),
                    () -> assertThat(result.getCity()).isEqualTo(DUMMY_CITY),
                    () -> assertThat(result.getStreet()).isEqualTo(DUMMY_STREET),
                    () -> assertThat(result.getStreetNumber()).isEqualTo(DUMMY_STREET_NUMBER),
                    () -> assertThat(result.getZipcode()).isEqualTo(DUMMY_ZIPCODE),
                    () -> assertThat(result.getUserId()).isEqualTo(DUMMY_USER_ID)
            );
        }

        private AddressEntity prepareAddressEntity() {

            AddressEntity address = new AddressEntity();
            address.setId(DUMMY_ADDRESS_ID);
            address.setCity(DUMMY_CITY);
            address.setStreet(DUMMY_STREET);
            address.setStreetNumber(DUMMY_STREET_NUMBER);
            address.setZipcode(DUMMY_ZIPCODE);

            UserEntity user = new UserEntity();
            user.setId(DUMMY_USER_ID);
            user.setFirstName("Firstname");
            user.setLastName("Lastname");
            address.setUser(user);

            return address;
        }
    }

}