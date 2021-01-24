package cz.johnczek.dpapi.item.service;

import cz.johnczek.dpapi.item.dto.ItemDto;
import cz.johnczek.dpapi.item.request.ItemChangeDeliveryRequest;
import cz.johnczek.dpapi.item.request.ItemChangePaymentRequest;
import cz.johnczek.dpapi.item.request.ItemChangePictureRequest;
import cz.johnczek.dpapi.item.request.ItemChangeRequest;
import lombok.NonNull;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ItemService {

    List<ItemDto> findAllActive();

    Optional<ItemDto> findByItemId(@NonNull long itemId);

    List<ItemDto> findByItemIds(@NonNull Set<Long> itemIds);

    void changeItemDelivery(
            long itemId,
            @NonNull ItemChangeDeliveryRequest request);

    void changeItemPaymentMethod(long itemId, @NonNull ItemChangePaymentRequest request);

    void changeItem(long itemId, @NonNull ItemChangeRequest request);

    void changePicture(long itemId, @NonNull ItemChangePictureRequest request);

    void topItem(long itemId);

    void cancelItem(long itemId);
}
