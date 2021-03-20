package cz.johnczek.dpapi.item.service;

import cz.johnczek.dpapi.AbstractIntegrationTest;
import cz.johnczek.dpapi.item.dto.ItemHighestBidDto;
import cz.johnczek.dpapi.item.entity.ItemBidEntity;
import cz.johnczek.dpapi.item.entity.ItemEntity;
import cz.johnczek.dpapi.item.repository.ItemBidRepository;
import cz.johnczek.dpapi.item.repository.ItemRepository;
import cz.johnczek.dpapi.user.entity.UserEntity;
import cz.johnczek.dpapi.user.repository.UserRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class ItemBidServiceImplIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private ItemBidServiceImpl instance;

    @Autowired
    private ItemBidRepository itemBidRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Nested
    class FindHighestBidByItemId {

        @Test
        void nonExistingItem_emptyOptional() {

            Optional<ItemHighestBidDto> result = instance.findHighestBidByItemId(100L);

            assertThat(result).isEmpty();
        }

        @Test
        void existingItemWithNoBids_emptyOptional() {

            Optional<ItemHighestBidDto> result = instance.findHighestBidByItemId(2L);

            assertThat(result).isEmpty();
        }

        @Test
        void itemWithBids_highestBidDto() {

            long itemId = 1L;
            Optional<ItemHighestBidDto> resultOpt = instance.findHighestBidByItemId(1L);

            assertThat(resultOpt).isNotEmpty();

            ItemHighestBidDto result = resultOpt.get();
            assertThat(result.getItemId()).isEqualTo(itemId);
            assertThat(result.getAmount()).isEqualByComparingTo(BigDecimal.valueOf(106L));
        }
    }

    @Nested
    class FindHighestBidByItemIds {

        @Test
        void noInput_emptyMap() {

            Map<Long, ItemHighestBidDto> result = instance.findHighestBidByItemIds(Collections.emptySet());

            assertThat(result).isEmpty();
        }

        @Test
        void oneItemWithoutBids_emptyMap() {

            Map<Long, ItemHighestBidDto> result = instance.findHighestBidByItemIds(Set.of(2L));

            assertThat(result).isEmpty();
        }

        @Test
        void oneItemWithBids_mapWithOneRecord() {

            long itemId = 1L;
            Map<Long, ItemHighestBidDto> resultMap = instance.findHighestBidByItemIds(Set.of(itemId));

            assertThat(resultMap).hasSize(1);
            ItemHighestBidDto result = resultMap.get(itemId);
            assertThat(result.getItemId()).isEqualTo(itemId);
            assertThat(result.getAmount()).isEqualByComparingTo(BigDecimal.valueOf(106L));
        }

        @Test
        void oneItemWithBidsAndOneWithoutBids_mapWithOneRecord() {

            long firstItemId = 1L;
            long secondItemId = 2L;
            Map<Long, ItemHighestBidDto> resultMap = instance.findHighestBidByItemIds(Set.of(firstItemId, secondItemId));

            assertThat(resultMap).hasSize(1);
            ItemHighestBidDto result = resultMap.get(firstItemId);
            assertThat(result.getItemId()).isEqualTo(firstItemId);
            assertThat(result.getAmount()).isEqualByComparingTo(BigDecimal.valueOf(106L));
        }
    }

    @Nested
    class createBid {

        @Test
        void validData_bidCreated() {

            LocalDateTime now = LocalDateTime.now();
            ItemEntity item = itemRepository.findById(2L).orElse(null);
            UserEntity user = userRepository.findById(3L).orElse(null);

            assertThat(item).isNotNull();
            assertThat(user).isNotNull();

            ItemHighestBidDto result = instance.createBid(item, user, BigDecimal.TEN, now).orElse(null);
            assertThat(result).isNotNull();

            assertAll(
                    () -> assertThat(result.getAmount()).isNotNull(),
                    () -> assertThat(result.getAmount()).isEqualTo(BigDecimal.TEN),
                    () -> assertThat(result.getTime()).isNotNull().isEqualTo(now),
                    () -> assertThat(result.getUserId()).isEqualTo(user.getId()),
                    () -> assertThat(result.getItemId()).isEqualTo(item.getId())
            );

            // cleanup
            List<ItemBidEntity> bidsByItem = itemBidRepository.findBidsByItemOrderByCreated(item.getId());
            itemBidRepository.delete(bidsByItem.get(0));

        }
    }
}