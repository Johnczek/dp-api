package cz.johnczek.dpapi.item.service;

import cz.johnczek.dpapi.item.dto.ItemHighestBidDto;
import cz.johnczek.dpapi.item.entity.ItemEntity;
import cz.johnczek.dpapi.user.entity.UserEntity;
import lombok.NonNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface ItemBidService {

    /**
     * @param itemIds ids of items we want to retrieve highest bids for
     * @return map where key is item id and value is dto holding information about highest bid for this item
     */
    Map<Long, ItemHighestBidDto> findHighestBidByItemIds(@NonNull Set<Long> itemIds);

    /**
     * @param itemId id of item we wan to retrieve highest bid for
     * @return dto holding information about currently highest bid,
     * empty optional in case there are no bids on retrieval was not successful
     */
    Optional<ItemHighestBidDto> findHighestBidByItemId(long itemId);

    Optional<ItemHighestBidDto> createBid(@NonNull ItemEntity item,
                                          @NonNull UserEntity user,
                                          @NonNull BigDecimal amount,
                                          @NonNull LocalDateTime time);
}
