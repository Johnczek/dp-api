package cz.johnczek.dpapi.delivery.mapper;

import cz.johnczek.dpapi.delivery.dto.DeliveryDto;
import cz.johnczek.dpapi.delivery.entity.DeliveryEntity;
import cz.johnczek.dpapi.file.entity.FileEntity;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@ExtendWith(MockitoExtension.class)
class DeliveryMapperUnitTest {

    private static final long DUMMY_ID = 0L;
    private static final String DUMMY_NAME = "Dummy delivery name";
    private static final BigDecimal DUMMY_PRICE = BigDecimal.TEN;
    private static final String DUMMY_DESCRIPTION = "Dummy delivery description";
    private static final long DUMMY_FILE_ID = 100L;
    private static final String DUMMY_FILE_UUID = "2b04a2a3-e704-4ad9-bfeb-ce73d1ae5a75";


    @InjectMocks
    private DeliveryMapperImpl instance;
    
    @Nested
    class EntityToDto {
        
        @Test
        void nullInput_nullOutput() {

            DeliveryDto result = instance.entityToDto(null);
            
            assertThat(result).isNull();
        }

        @Test
        void validEntity_validDto() {

            DeliveryDto result = instance.entityToDto(prepareEntity());

            assertAll(
                    () -> assertThat(result).isNotNull(),
                    () -> assertThat(result.getId()).isEqualTo(DUMMY_ID),
                    () -> assertThat(result.getName()).isEqualTo(DUMMY_NAME),
                    () -> assertThat(result.getPrice()).isEqualTo(DUMMY_PRICE),
                    () -> assertThat(result.getDescription()).isEqualTo(DUMMY_DESCRIPTION),
                    () -> assertThat(result.getLogoUUID()).isEqualTo(DUMMY_FILE_UUID)
            );
        }

        private DeliveryEntity prepareEntity() {
            
            DeliveryEntity delivery = new DeliveryEntity();
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
