package cz.johnczek.dpapi.item.service;

import cz.johnczek.dpapi.item.dto.ItemDto;
import cz.johnczek.dpapi.item.entity.ItemEntity;
import cz.johnczek.dpapi.item.request.ItemChangeDeliveryRequest;
import cz.johnczek.dpapi.item.request.ItemChangePaymentRequest;
import cz.johnczek.dpapi.item.request.ItemChangePictureRequest;
import cz.johnczek.dpapi.item.request.ItemChangeRequest;
import cz.johnczek.dpapi.item.request.ItemCreationRequest;
import cz.johnczek.dpapi.item.response.ItemCreationOptionsResponse;
import cz.johnczek.dpapi.item.response.ItemEditOptionsResponse;
import lombok.NonNull;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface ItemService {

    List<ItemDto> findAllActive();

    Optional<ItemDto> findByItemId(long itemId);

    Optional<ItemEntity> findEntityById(long itemId);

    ItemEditOptionsResponse findByItemIdForEdit(long itemId);

    List<ItemDto> findByItemIds(@NonNull Set<Long> itemIds);

    Map<Long, ItemDto> findByItemIdsMap(@NonNull Set<Long> itemIds);

    List<ItemDto> findBySellerId(long sellerId);

    void changeItemDelivery(
            long itemId,
            @NonNull ItemChangeDeliveryRequest request);

    void changeItemPaymentMethod(long itemId, @NonNull ItemChangePaymentRequest request);

    void changeItem(long itemId, @NonNull ItemChangeRequest request);

    void changePicture(long itemId, @NonNull ItemChangePictureRequest request);

    void topItem(long itemId);

    void cancelItem(long itemId);

    ItemCreationOptionsResponse getItemCreationOptions();

    Optional<ItemDto> createItem(ItemCreationRequest request);

    List<ItemDto> findCartItemsForUser(long buyerId);

    void checkItemBuyability(long itemId);
}
