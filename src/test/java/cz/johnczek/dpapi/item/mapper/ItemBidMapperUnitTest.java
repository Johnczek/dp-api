package cz.johnczek.dpapi.item.mapper;

import cz.johnczek.dpapi.item.dto.ItemHighestBidDto;
import cz.johnczek.dpapi.item.entity.ItemBidEntity;
import cz.johnczek.dpapi.item.entity.ItemEntity;
import cz.johnczek.dpapi.user.entity.UserEntity;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@ExtendWith(MockitoExtension.class)
class ItemBidMapperUnitTest {

    private static final long DUMMY_ITEM_ID = 0L;
    private static final long DUMMY_USER_ID = 1L;
    private static final BigDecimal DUMMY_AMOUNT = new BigDecimal("123");
    private static final LocalDateTime DUMMY_TIME = LocalDateTime.now().minusSeconds(25);


    @InjectMocks
    private ItemBidMapperImpl instance;

    @Nested
    class EntityToHighestBidDto {

        @Test
        void nullInput_nullOutput() {

            ItemHighestBidDto result = instance.entityToHighestBidDto(null);

            assertThat(result).isNull();
        }

        @Test
        void validEntity_validDto() {

            ItemHighestBidDto result = instance.entityToHighestBidDto(prepareEntity());

            assertAll(
                    () -> assertThat(result).isNotNull(),
                    () -> assertThat(result.getItemId()).isEqualTo(DUMMY_ITEM_ID),
                    () -> assertThat(result.getUserId()).isEqualTo(DUMMY_USER_ID),
                    () -> assertThat(result.getAmount()).isEqualTo(DUMMY_AMOUNT),
                    () -> assertThat(result.getTime()).isEqualTo(DUMMY_TIME)
            );
        }

        private ItemBidEntity prepareEntity() {

            ItemBidEntity itemBid = new ItemBidEntity();
            itemBid.setTime(DUMMY_TIME);
            itemBid.setAmount(DUMMY_AMOUNT);

            UserEntity user = new UserEntity();
            user.setId(DUMMY_USER_ID);
            user.setFirstName("FirstName");
            itemBid.setBuyer(user);

            ItemEntity item = new ItemEntity();
            item.setId(DUMMY_ITEM_ID);
            item.setName("Item name");
            itemBid.setItem(item);

            return itemBid;
        }
    }

}