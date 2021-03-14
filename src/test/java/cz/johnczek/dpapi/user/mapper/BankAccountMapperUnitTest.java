package cz.johnczek.dpapi.user.mapper;

import cz.johnczek.dpapi.user.dto.BankAccountDto;
import cz.johnczek.dpapi.user.entity.BankAccountEntity;
import cz.johnczek.dpapi.user.entity.UserEntity;
import cz.johnczek.dpapi.user.request.BankAccountCreationRequest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@ExtendWith(MockitoExtension.class)
class BankAccountMapperUnitTest {

    private static final long DUMMY_USER_ID = 20L;
    private static final long DUMMY_BANK_ACCOUNT_ID = 10L;
    private static final long DUMMY_BANK_ACCOUNT_PREFIX = 1490L;
    private static final long DUMMY_BANK_ACCOUNT_NUMBER = 10101010L;
    private static final int DUMMY_BANK_ACCOUNT_BANK_CODE = 3030;

    @InjectMocks
    private BankAccountMapperImpl instance;

    @Nested
    class RequestToEntity {

        @Test
        void nullInput_nullOutput() {

            BankAccountEntity result = instance.requestToEntity(null);

            assertThat(result).isNull();
        }

        @Test
        void validInput_validEntity() {

            BankAccountEntity result = instance.requestToEntity(prepareDummyRequest());

            assertAll(
                    () -> assertThat(result).isNotNull(),
                    () -> assertThat(result.getPrefix()).isEqualTo(DUMMY_BANK_ACCOUNT_PREFIX),
                    () -> assertThat(result.getNumber()).isEqualTo(DUMMY_BANK_ACCOUNT_NUMBER),
                    () -> assertThat(result.getBankCode()).isEqualTo(DUMMY_BANK_ACCOUNT_BANK_CODE)
            );
        }

        private BankAccountCreationRequest prepareDummyRequest() {

            BankAccountCreationRequest request = new BankAccountCreationRequest();
            request.setPrefix(DUMMY_BANK_ACCOUNT_PREFIX);
            request.setNumber(DUMMY_BANK_ACCOUNT_NUMBER);
            request.setBankCode(DUMMY_BANK_ACCOUNT_BANK_CODE);

            return request;
        }
    }

    @Nested
    class EntityToDto {

        @Test
        void nullInput_nullOutput() {

            BankAccountDto result = instance.entityToDto(null);

            assertThat(result).isNull();
        }

        @Test
        void validInput_validEntity() {

            BankAccountDto result = instance.entityToDto(prepareBankAccountEntity());

            assertAll(
                    () -> assertThat(result).isNotNull(),
                    () -> assertThat(result.getId()).isEqualTo(DUMMY_BANK_ACCOUNT_ID),
                    () -> assertThat(result.getPrefix()).isEqualTo(DUMMY_BANK_ACCOUNT_PREFIX),
                    () -> assertThat(result.getNumber()).isEqualTo(DUMMY_BANK_ACCOUNT_NUMBER),
                    () -> assertThat(result.getBankCode()).isEqualTo(DUMMY_BANK_ACCOUNT_BANK_CODE),
                    () -> assertThat(result.getUserId()).isEqualTo(DUMMY_USER_ID)
            );
        }

        private BankAccountEntity prepareBankAccountEntity() {

            BankAccountEntity address = new BankAccountEntity();
            address.setId(DUMMY_BANK_ACCOUNT_ID);
            address.setPrefix(DUMMY_BANK_ACCOUNT_PREFIX);
            address.setNumber(DUMMY_BANK_ACCOUNT_NUMBER);
            address.setBankCode(DUMMY_BANK_ACCOUNT_BANK_CODE);

            UserEntity user = new UserEntity();
            user.setId(DUMMY_USER_ID);
            user.setFirstName("Firstname");
            user.setLastName("Lastname");
            address.setUser(user);

            return address;
        }
    }

}