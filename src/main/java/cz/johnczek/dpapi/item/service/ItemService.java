package cz.johnczek.dpapi.item.service;

import cz.johnczek.dpapi.item.dto.ItemDto;
import lombok.NonNull;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ItemService {

    List<ItemDto> findAll();

    Optional<ItemDto> findByItemId(@NonNull long itemId);

    List<ItemDto> findByItemIds(@NonNull Set<Long> itemIds);
}
