package cz.johnczek.dpapi.payment.service;

import cz.johnczek.dpapi.AbstractIntegrationTest;
import cz.johnczek.dpapi.payment.dto.PaymentDto;
import cz.johnczek.dpapi.payment.entity.PaymentEntity;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class PaymentServiceImplIntegrationTest extends AbstractIntegrationTest {


    @Autowired
    private PaymentServiceImpl instance;

    @Nested
    class GetAllPaymentTypes {

        @Test
        void allBasicPaymentTypes_2() {

            assertThat(instance.getAllPaymentTypes())
                    .isNotEmpty()
                    .hasSize(3);
        }
    }

    @Nested
    class FindById {

        @Test
        void notValidId_noRecordFound() {
            Optional<PaymentEntity> byId = instance.findById(200L);

            assertThat(byId).isEmpty();
        }

        @Test
        void validId_recordFound() {
            Optional<PaymentEntity> byId = instance.findById(2L);

            assertThat(byId).isNotEmpty();
        }
    }

    @Nested
    class FindByItemIds {

        @Test
        void emptyInput_noRecordsFound() {
            instance.findByItemIds(Collections.emptySet());

            assertThat(instance.findByItemIds(Collections.emptySet())).isEmpty();
        }

        @Test
        void oneValidItemInput_oneRecordFound() {
            long itemId = 1L;
            Map<Long, PaymentDto> resultMap = instance.findByItemIds(Collections.singleton(itemId));

            assertThat(resultMap).isNotEmpty();

            PaymentDto firstItem = resultMap.get(itemId);
            assertThat(firstItem).isNotNull();
            assertThat(firstItem.getId()).isEqualTo(1L);
        }

        @Test
        void oneInvalidItemInput_noRecordsFound() {
            long itemId = 100L;
            Map<Long, PaymentDto> resultMap = instance.findByItemIds(Collections.singleton(itemId));

            assertThat(resultMap).isEmpty();
        }

        @Test
        void oneValidAndOneInvalidItemInput_oneRecordFound() {
            long validItemId = 1L;
            long invalidItemId = 1111L;
            Map<Long, PaymentDto> resultMap = instance.findByItemIds(Set.of(validItemId, invalidItemId));

            assertThat(resultMap).isNotEmpty().hasSize(1);

            PaymentDto firstItem = resultMap.get(validItemId);
            assertThat(firstItem).isNotNull();
            assertThat(firstItem.getId()).isEqualTo(1L);
        }

        @Test
        void twoValidInputsWithDifferentDeliveries_twoRecordsFound() {
            long firstValidItemId = 1L;
            long secondValidItemId = 3L;
            Map<Long, PaymentDto> resultMap = instance.findByItemIds(Set.of(firstValidItemId, secondValidItemId));

            assertThat(resultMap).isNotEmpty().hasSize(2);

            PaymentDto firstItem = resultMap.get(1L);
            assertThat(firstItem).isNotNull();
            assertThat(firstItem.getId()).isEqualTo(1L);

            PaymentDto secondItem = resultMap.get(2L);
            assertThat(secondItem).isNotNull();
            assertThat(secondItem.getId()).isEqualTo(2L);
        }

        @Test
        void twoValidInputsWithSamePayment_oneRecordFound() {
            long firstValidItemId = 2L;
            long secondValidItemId = 3L;
            Map<Long, PaymentDto> resultMap = instance.findByItemIds(Set.of(firstValidItemId, secondValidItemId));

            assertThat(resultMap).isNotEmpty().hasSize(1);

            PaymentDto firstItem = resultMap.get(2L);
            assertThat(firstItem).isNotNull();
            assertThat(firstItem.getId()).isEqualTo(2L);
        }
    }
}