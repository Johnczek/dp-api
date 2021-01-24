package cz.johnczek.dpapi.item.service;

import cz.johnczek.dpapi.item.dto.ItemHighestBidDto;
import lombok.NonNull;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface ItemBidService {

    Map<Long, ItemHighestBidDto> findHighestBidByItemIds(@NonNull Set<Long> itemIds);

    Optional<ItemHighestBidDto> findHighestBidByItemId(long itemId);
}
