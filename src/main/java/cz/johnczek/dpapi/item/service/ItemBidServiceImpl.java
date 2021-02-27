package cz.johnczek.dpapi.item.service;

import cz.johnczek.dpapi.item.dto.ItemHighestBidDto;
import cz.johnczek.dpapi.item.repository.ItemBidRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemBidServiceImpl implements ItemBidService {

    private final ItemBidRepository itemBidRepository;

    @Override
    @Transactional(readOnly = true)
    public Map<Long, ItemHighestBidDto> findHighestBidByItemIds(@NonNull Set<Long> itemIds) {

        if (CollectionUtils.isEmpty(itemIds)) {
            return Collections.emptyMap();
        }

        return itemBidRepository.findBidsByItemIds(itemIds).stream()
                .collect(Collectors.toMap(
                        ItemHighestBidDto::getItemId,
                        Function.identity(),
                        (a, b) -> a.getAmount().compareTo(b.getAmount()) >= 0 ? a : b));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ItemHighestBidDto> findHighestBidByItemId(long itemId) {
        Map<Long, ItemHighestBidDto> highestBidByItemIds = findHighestBidByItemIds(Collections.singleton(itemId));

        ItemHighestBidDto itemHighestBidDto = highestBidByItemIds.get(itemId);
        if (itemHighestBidDto != null) {
            return Optional.of(itemHighestBidDto);
        } else {
            return Optional.empty();
        }
    }
}
