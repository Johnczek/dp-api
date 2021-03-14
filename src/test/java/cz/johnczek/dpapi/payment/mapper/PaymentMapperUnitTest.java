package cz.johnczek.dpapi.payment.mapper;

import cz.johnczek.dpapi.file.entity.FileEntity;
import cz.johnczek.dpapi.payment.dto.PaymentDto;
import cz.johnczek.dpapi.payment.entity.PaymentEntity;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@ExtendWith(MockitoExtension.class)
class PaymentMapperUnitTest {

    private static final long DUMMY_ID = 5L;
    private static final String DUMMY_NAME = "Dummy payment name";
    private static final BigDecimal DUMMY_PRICE = BigDecimal.ZERO;
    private static final String DUMMY_DESCRIPTION = "Dummy payment description";
    private static final long DUMMY_FILE_ID = 1000L;
    private static final String DUMMY_FILE_UUID = "d77ee881-ba8d-4221-9d7d-8b79f959e601";


    @InjectMocks
    private PaymentMapperImpl instance;

    @Nested
    class EntityToDto {

        @Test
        void nullInput_nullOutput() {

            PaymentDto result = instance.entityToDto(null);

            assertThat(result).isNull();
        }

        @Test
        void validEntity_validDto() {

            PaymentDto result = instance.entityToDto(prepareEntity());

            assertAll(
                    () -> assertThat(result).isNotNull(),
                    () -> assertThat(result.getId()).isEqualTo(DUMMY_ID),
                    () -> assertThat(result.getName()).isEqualTo(DUMMY_NAME),
                    () -> assertThat(result.getPrice()).isEqualTo(DUMMY_PRICE),
                    () -> assertThat(result.getDescription()).isEqualTo(DUMMY_DESCRIPTION),
                    () -> assertThat(result.getLogoUUID()).isEqualTo(DUMMY_FILE_UUID)
            );
        }

        private PaymentEntity prepareEntity() {

            PaymentEntity delivery = new PaymentEntity();
            delivery.setId(DUMMY_ID);
            delivery.setName(DUMMY_NAME);
            delivery.setPrice(DUMMY_PRICE);
            delivery.setDescription(DUMMY_DESCRIPTION);

            FileEntity file = new FileEntity();
            file.setId(DUMMY_FILE_ID);
            file.setFileIdentifier(DUMMY_FILE_UUID);
            delivery.setLogo(file);

            return delivery;
        }
    }
}