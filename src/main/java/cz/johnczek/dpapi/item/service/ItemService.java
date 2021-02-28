package cz.johnczek.dpapi.item.service;

import cz.johnczek.dpapi.core.errorhandling.exception.BaseForbiddenRestException;
import cz.johnczek.dpapi.core.errorhandling.exception.DeliveryNotFoundRestException;
import cz.johnczek.dpapi.core.errorhandling.exception.FileNotFoundRestException;
import cz.johnczek.dpapi.core.errorhandling.exception.ItemNotBuyableRestException;
import cz.johnczek.dpapi.core.errorhandling.exception.ItemNotFoundRestException;
import cz.johnczek.dpapi.core.errorhandling.exception.PaymentNotFoundRestException;
import cz.johnczek.dpapi.core.errorhandling.exception.UserNotFoundRestException;
import cz.johnczek.dpapi.item.dto.ItemDto;
import cz.johnczek.dpapi.item.entity.ItemEntity;
import cz.johnczek.dpapi.item.enums.ItemState;
import cz.johnczek.dpapi.item.request.ItemChangeDeliveryRequest;
import cz.johnczek.dpapi.item.request.ItemChangePaymentRequest;
import cz.johnczek.dpapi.item.request.ItemChangePictureRequest;
import cz.johnczek.dpapi.item.request.ItemChangeRequest;
import cz.johnczek.dpapi.item.request.ItemCreationRequest;
import cz.johnczek.dpapi.item.request.ItemWsBidRequest;
import cz.johnczek.dpapi.item.response.ItemCreationOptionsResponse;
import cz.johnczek.dpapi.item.response.ItemEditOptionsResponse;
import cz.johnczek.dpapi.item.response.ItemWsInfoResponse;
import lombok.NonNull;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface ItemService {

    /**
     * @return all currently active (items that are open for bids) items
     */
    List<ItemDto> findAllActive();

    /**
     * @param itemId id of item we want to find
     * @return item data dto, empty optional if item was not found or retrieval was not successful
     */
    Optional<ItemDto> findByItemId(long itemId);

    /**
     * @param itemId id of item we want to find
     * @return item entity, empty optional if item was not found or retrieval was not successful
     */
    Optional<ItemEntity> findEntityById(long itemId);

    /**
     * @param itemId id of item we want to find
     * @return object holding item data and possible edit configurations,
     * empty optional if item was not found or retrieval was not successful
     * @throws ItemNotFoundRestException if item could not be found
     */
    ItemEditOptionsResponse findByItemIdForEdit(long itemId);

    /**
     * @param itemIds ids of items we want to find
     * @return list of item entities by given ids
     */
    List<ItemDto> findByItemIds(@NonNull Set<Long> itemIds);

    /**
     *
     * @param itemIds ids of items we want to find
     * @return map where key is item id and value is corresponding item dto
     */
    Map<Long, ItemDto> findByItemIdsMap(@NonNull Set<Long> itemIds);

    /**
     * @param sellerId id of seller for which we want to search items
     * @return list of items where seller is given user
     */
    List<ItemDto> findBySellerId(long sellerId);

    /**
     * @param itemId id of item we want to change delivery
     * @param request request holding new delivery data
     * @throws ItemNotFoundRestException in case that item could not be found
     * @throws DeliveryNotFoundRestException in case that given delivery could not be found
     */
    void changeItemDelivery(
            long itemId,
            @NonNull ItemChangeDeliveryRequest request);

    /**
     * @param itemId id of item we want to change payment
     * @param request request holding new payment data
     * @throws ItemNotFoundRestException in case that item could not be found
     * @throws PaymentNotFoundRestException in case that given payment could not be found
     */
    void changeItemPaymentMethod(long itemId, @NonNull ItemChangePaymentRequest request);

    /**
     * @param itemId id of item we want to change item general info
     * @param request request holding new item data
     * @throws ItemNotFoundRestException in case that item could not be found
     */
    void changeItem(long itemId, @NonNull ItemChangeRequest request);

    /**
     * @param itemId id of item we want to change picture
     * @param request request holding new item picture
     * @throws ItemNotFoundRestException in case that item could not be found
     * @throws FileNotFoundRestException in case that given file could not be found in the system
     */
    void changePicture(long itemId, @NonNull ItemChangePictureRequest request);

    /**
     * Method sets item as topped
     * @param itemId id of item we want to top
     * @throws ItemNotFoundRestException in case that item could not be found
     */
    void topItem(long itemId);

    /**
     * Method sets item state to {@link ItemState#CANCELLED} if its in state from which is can be cancelled.
     *
     * @param itemId id of item we want to cancel
     * @throws ItemNotFoundRestException in case that item could not be found
     */
    void cancelItem(long itemId);

    /**
     * @return data needed for item creation (possbile payment methods, delivery methods, ...)
     */
    ItemCreationOptionsResponse getItemCreationOptions();

    /**
     * @param request object holding all item data needed for its creation
     * @return data of newly created item, empty optional in case creation was not successful
     * @throws BaseForbiddenRestException in case user is not logged in
     * @throws UserNotFoundRestException in case logged user has no record in database
     * @throws PaymentNotFoundRestException in case that chosen payment was not found
     * @throws DeliveryNotFoundRestException in case that chosen delivery was not found
     * @throws FileNotFoundRestException if given file identifier could not be found in the system
     */
    Optional<ItemDto> createItem(ItemCreationRequest request);

    /**
     * @param buyerId id of user we are searching for his cart items
     * @return list of items that are in state that corresponds cart item buy process
     */
    List<ItemDto> findCartItemsForUser(long buyerId);

    /**
     * Method checks, if item with given id is in state where it can be bought. Throws exceptions in case it is
     * @param itemId id of item we want to check
     * @throws BaseForbiddenRestException in case user is not logged in or does not have highest bid on item
     * (and therefore it is not able to buy it)
     * @throws ItemNotBuyableRestException in case we are unable to find highest bid for given item
     */
    void checkItemBuyability(long itemId);

    /**
     * @see ItemBidService#findHighestBidByItemId(long)
     * @return dto of current highest bid and current item state
     */
    ItemWsInfoResponse findHighestBidByItemId(long itemId);

    Optional<ItemWsInfoResponse> bid(long itemId, @NonNull ItemWsBidRequest request, LocalDateTime currentTime);
}
