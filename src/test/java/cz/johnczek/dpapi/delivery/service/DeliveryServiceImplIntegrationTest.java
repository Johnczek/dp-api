package cz.johnczek.dpapi.delivery.service;

import cz.johnczek.dpapi.AbstractIntegrationTest;
import cz.johnczek.dpapi.delivery.dto.DeliveryDto;
import cz.johnczek.dpapi.delivery.entity.DeliveryEntity;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class DeliveryServiceImplIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private DeliveryServiceImpl instance;

    @Nested
    class GetAllDeliveryTypes {

        @Test
        void allBasicDeliveryTypes_2() {

            assertThat(instance.getAllDeliveryTypes())
                    .isNotEmpty()
                    .hasSize(2);
        }
    }

    @Nested
    class FindById {

        @Test
        void notValidId_noRecordFound() {
            Optional<DeliveryEntity> byId = instance.findById(200L);

            assertThat(byId).isEmpty();
        }

        @Test
        void validId_recordFound() {
            Optional<DeliveryEntity> byId = instance.findById(2L);

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
            Map<Long, DeliveryDto> resultMap = instance.findByItemIds(Collections.singleton(itemId));

            assertThat(resultMap).isNotEmpty();

            DeliveryDto firstItem = resultMap.get(itemId);
            assertThat(firstItem).isNotNull();
            assertThat(firstItem.getId()).isEqualTo(1L);
        }

        @Test
        void oneInvalidItemInput_noRecordsFound() {
            long itemId = 100L;
            Map<Long, DeliveryDto> resultMap = instance.findByItemIds(Collections.singleton(itemId));

            assertThat(resultMap).isEmpty();
        }

        @Test
        void oneValidAndOneInvalidItemInput_oneRecordFound() {
            long validItemId = 1L;
            long invalidItemId = 1111L;
            Map<Long, DeliveryDto> resultMap = instance.findByItemIds(Set.of(validItemId, invalidItemId));

            assertThat(resultMap).isNotEmpty().hasSize(1);

            DeliveryDto firstItem = resultMap.get(validItemId);
            assertThat(firstItem).isNotNull();
            assertThat(firstItem.getId()).isEqualTo(1L);
        }

        @Test
        void twoValidInputsWithDifferentDeliveries_twoRecordsFound() {
            long firstValidItemId = 1L;
            long secondValidItemId = 3L;
            Map<Long, DeliveryDto> resultMap = instance.findByItemIds(Set.of(firstValidItemId, secondValidItemId));

            assertThat(resultMap).isNotEmpty().hasSize(2);

            DeliveryDto firstItem = resultMap.get(1L);
            assertThat(firstItem).isNotNull();
            assertThat(firstItem.getId()).isEqualTo(1L);

            DeliveryDto secondItem = resultMap.get(2L);
            assertThat(secondItem).isNotNull();
            assertThat(secondItem.getId()).isEqualTo(2L);
        }

        @Test
        void twoValidInputsWithSameDelivery_oneRecordFound() {
            long firstValidItemId = 1L;
            long secondValidItemId = 2L;
            Map<Long, DeliveryDto> resultMap = instance.findByItemIds(Set.of(firstValidItemId, secondValidItemId));

            assertThat(resultMap).isNotEmpty().hasSize(1);

            DeliveryDto firstItem = resultMap.get(1L);
            assertThat(firstItem).isNotNull();
            assertThat(firstItem.getId()).isEqualTo(1L);
        }
    }
}