package cz.johnczek.dpapi.item.service;

import cz.johnczek.dpapi.core.errorhandling.exception.BaseForbiddenRestException;
import cz.johnczek.dpapi.core.errorhandling.exception.DeliveryNotFoundRestException;
import cz.johnczek.dpapi.core.errorhandling.exception.FileNotFoundRestException;
import cz.johnczek.dpapi.core.errorhandling.exception.ItemInNotEditableStateRestException;
import cz.johnczek.dpapi.core.errorhandling.exception.ItemNotBuyableRestException;
import cz.johnczek.dpapi.core.errorhandling.exception.ItemNotFoundRestException;
import cz.johnczek.dpapi.core.errorhandling.exception.PaymentNotFoundRestException;
import cz.johnczek.dpapi.core.errorhandling.exception.UserNotFoundRestException;
import cz.johnczek.dpapi.core.persistence.AbstractIdBasedEntity;
import cz.johnczek.dpapi.core.security.SecurityUtils;
import cz.johnczek.dpapi.delivery.dto.DeliveryDto;
import cz.johnczek.dpapi.delivery.entity.DeliveryEntity;
import cz.johnczek.dpapi.delivery.service.DeliveryService;
import cz.johnczek.dpapi.file.entity.FileEntity;
import cz.johnczek.dpapi.file.service.FileService;
import cz.johnczek.dpapi.item.dto.ItemDto;
import cz.johnczek.dpapi.item.dto.ItemHighestBidDto;
import cz.johnczek.dpapi.item.entity.ItemEntity;
import cz.johnczek.dpapi.item.enums.ItemState;
import cz.johnczek.dpapi.item.mapper.ItemMapper;
import cz.johnczek.dpapi.item.repository.ItemRepository;
import cz.johnczek.dpapi.item.request.ItemChangeDeliveryRequest;
import cz.johnczek.dpapi.item.request.ItemChangePaymentRequest;
import cz.johnczek.dpapi.item.request.ItemChangePictureRequest;
import cz.johnczek.dpapi.item.request.ItemChangeRequest;
import cz.johnczek.dpapi.item.request.ItemCreationRequest;
import cz.johnczek.dpapi.item.response.ItemCreationOptionsResponse;
import cz.johnczek.dpapi.item.response.ItemEditOptionsResponse;
import cz.johnczek.dpapi.payment.dto.PaymentDto;
import cz.johnczek.dpapi.payment.entity.PaymentEntity;
import cz.johnczek.dpapi.payment.service.PaymentService;
import cz.johnczek.dpapi.user.dto.LoggedUserDetails;
import cz.johnczek.dpapi.user.dto.UserDto;
import cz.johnczek.dpapi.user.entity.UserEntity;
import cz.johnczek.dpapi.user.service.UserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    private final DeliveryService deliveryService;

    private final PaymentService paymentService;

    private final UserService userService;

    private final ItemBidService itemBidService;

    private final FileService fileService;

    private final ItemMapper itemMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ItemDto> findByItemIds(@NonNull Set<Long> itemIds) {

        if (CollectionUtils.isEmpty(itemIds)) {
            return Collections.emptyList();
        }

        Map<Long, PaymentDto> paymentsMap = paymentService.findByItemIds(itemIds);
        Map<Long, DeliveryDto> deliveriesMap = deliveryService.findByItemIds(itemIds);
        Map<Long, UserDto> usersMap = userService.findByItemIds(itemIds);
        Map<Long, ItemHighestBidDto> itemHighestBidDtoMap = itemBidService.findHighestBidByItemIds(itemIds);

        return itemRepository.findByItemIdsWithFields(itemIds).stream()
                .map(i -> itemMapper.entityToDto(
                        i,
                        deliveriesMap.get(i.getDelivery().getId()),
                        paymentsMap.get(i.getPayment().getId()),
                        usersMap.get(i.getSeller().getId()),
                        itemHighestBidDtoMap.get(i.getId())))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemDto> findBySellerId(long sellerId) {
        Set<Long> itemIds = itemRepository.findAllActiveIdsBySellerId(sellerId);
        if (CollectionUtils.isEmpty(itemIds)) {
            return Collections.emptyList();
        }

        return findByItemIds(itemIds);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemDto> findAllActive() {
        Set<Long> itemIds = itemRepository.findAllActiveIds();
        if (CollectionUtils.isEmpty(itemIds)) {
            return Collections.emptyList();
        }

        return findByItemIds(itemIds);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ItemDto> findByItemId(long itemId) {
        return findByItemIds(Collections.singleton(itemId)).stream()
                .findFirst();
    }

    @Override
    @Transactional(readOnly = true)
    public ItemEditOptionsResponse findByItemIdForEdit(long itemId) {
        ItemDto item = findByItemId(itemId).orElseThrow(() -> {

            log.error("Could not retrieve item for item edit, item with id {} not found", itemId);

            return new ItemNotFoundRestException(itemId);
        });

        return ItemEditOptionsResponse.builder()
                .item(item)
                .payments(paymentService.getAllPaymentTypes())
                .deliveries(deliveryService.getAllDeliveryTypes())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public ItemCreationOptionsResponse getItemCreationOptions() {
        return ItemCreationOptionsResponse.builder()
                .payments(paymentService.getAllPaymentTypes())
                .deliveries(deliveryService.getAllDeliveryTypes())
                .build();
    }

    @Override
    @Transactional
    public Optional<ItemDto> createItem(ItemCreationRequest request) {

        ItemEntity item = itemMapper.creationRequestToEntity(request);
        item.setState(ItemState.ACTIVE);

        LoggedUserDetails loggedUser = SecurityUtils.getLoggedUser().orElseThrow(() -> {
            log.error("Creation of item failed. Logged person not found");

            return new BaseForbiddenRestException();
        });

        UserEntity user = userService.findEntityById(loggedUser.getId()).orElseThrow(() -> {
            log.error("Creation of item failed. Logged person with id {} not found", loggedUser.getId());

            return new UserNotFoundRestException(loggedUser.getId());
        });
        item.setSeller(user);

        PaymentEntity payment = paymentService.findById(request.getPaymentId()).orElseThrow(() -> {
            log.error("Creation of item failed. Payment method with id {} not found", request.getPaymentId());

            return new PaymentNotFoundRestException(request.getPaymentId());
        });
        item.setPayment(payment);

        DeliveryEntity delivery = deliveryService.findById(request.getDeliveryId()).orElseThrow(() -> {
            log.error("Creation of item failed. Delivery with id {} not found", request.getDeliveryId());

            return new DeliveryNotFoundRestException(request.getDeliveryId());
        });
        item.setDelivery(delivery);

        FileEntity picture = fileService.findByFileIdentifier(request.getPictureUUID()).orElseThrow(() -> {
            log.error("Creation of item failed. Picture with uuid {} not found", request.getPictureUUID());

            return new FileNotFoundRestException(request.getPictureUUID());
        });
        item.setPicture(picture);

        itemRepository.save(item);

        return findByItemId(item.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemDto> findCartItemsForUser(long buyerId) {

        Set<Long> itemIds = itemRepository.findAllIdsForCartByBuyerId(buyerId);
        if (CollectionUtils.isEmpty(itemIds)) {
            return Collections.emptyList();
        }

        return findByItemIds(itemIds);
    }

    @Override
    @Transactional(readOnly = true)
    public void checkItemBuyability(long itemId) {
        LoggedUserDetails loggedUser = SecurityUtils.getLoggedUser().orElseThrow(() -> {
            log.error("Getting basket item for item with id {} failed. Logged user not found", itemId);

            return new BaseForbiddenRestException();
        });

        ItemHighestBidDto itemHighestBid = itemBidService.findHighestBidByItemId(itemId).orElseThrow(() -> {
            log.error("Getting basket item for item with id {} failed. No bids found for item", itemId);

            return new ItemNotBuyableRestException();
        });

        if (!itemHighestBid.getUserId().equals(loggedUser.getId())) {
            throw new BaseForbiddenRestException();
        }
    }

    @Override
    @Transactional
    public void changeItemDelivery(long itemId, @NonNull ItemChangeDeliveryRequest request) {

        ItemEntity item = itemRepository.findByIdWithFieldsFetched(itemId).orElseThrow(() -> {

            log.error("Update delivery of item with id {} failed. Item not found", itemId);

            return new ItemNotFoundRestException(itemId);
        });

        checkItemEditability(item);
        checkLoggedPersonPermissionToItem(item);

        Long deliveryId = request.getDeliveryId();
        DeliveryEntity delivery = deliveryService.findById(deliveryId).orElseThrow(() -> {

            log.error("Update delivery of item with id {} failed. Delivery with id {} not found",
                    itemId,
                    deliveryId);

            return new DeliveryNotFoundRestException(deliveryId);
        });

        item.setDelivery(delivery);
    }

    @Override
    @Transactional
    public void changeItemPaymentMethod(long itemId, @NonNull ItemChangePaymentRequest request) {

        ItemEntity item = itemRepository.findByIdWithFieldsFetched(itemId).orElseThrow(() -> {

            log.error("Update payment of item with id {} failed. Item not found", itemId);

            return new ItemNotFoundRestException(itemId);
        });

        checkItemEditability(item);
        checkLoggedPersonPermissionToItem(item);

        Long paymentId = request.getPaymentId();
        PaymentEntity payment = paymentService.findById(paymentId).orElseThrow(() -> {

            log.error("Update payment of item with id {} failed. Payment with id {} not found",
                    itemId,
                    paymentId);

            return new PaymentNotFoundRestException(paymentId);
        });

        item.setPayment(payment);
    }

    @Override
    @Transactional
    public void changeItem(long itemId, @NonNull ItemChangeRequest request) {

        ItemEntity item = itemRepository.findByIdWithFieldsFetched(itemId).orElseThrow(() -> {

            log.error("Update of item with id {} failed. Item not found", itemId);

            return new ItemNotFoundRestException(itemId);
        });

        checkItemEditability(item);
        checkLoggedPersonPermissionToItem(item);

        if (StringUtils.isNotBlank(request.getDescription())) {
            item.setDescription(request.getDescription());
        }

        if (StringUtils.isNotBlank(request.getName())) {
            item.setName(request.getName());
        }

        if (request.getValidFrom() != null) {
            item.setValidFrom(request.getValidFrom());
        }

        if (request.getValidTo() != null) {
            item.setValidTo(request.getValidTo());
        }
    }

    @Override
    @Transactional
    public void changePicture(long itemId, @NonNull ItemChangePictureRequest request) {

        ItemEntity item = itemRepository.findByIdWithFieldsFetched(itemId).orElseThrow(() -> {

            log.error("Update payment of item with id {} failed. Item not found", itemId);

            return new ItemNotFoundRestException(itemId);
        });

        checkItemEditability(item);
        checkLoggedPersonPermissionToItem(item);

        String pictureUUID = request.getPictureUUID();
        FileEntity file = fileService.findByFileIdentifier(pictureUUID).orElseThrow(() -> {

            log.error("Update picture of item with id {} failed. Item with identifier {} not found", itemId, pictureUUID);

            return new FileNotFoundRestException(pictureUUID);
        });

        item.setPicture(file);
    }

    @Override
    @Transactional
    public void topItem(long itemId) {

        ItemEntity item = itemRepository.findByIdWithFieldsFetched(itemId).orElseThrow(() -> {

            log.error("Topping of item with id {} failed. Item not found", itemId);

            return new ItemNotFoundRestException(itemId);
        });

        checkItemEditability(item);
        checkLoggedPersonPermissionToItem(item);

        item.setTopped(true);
    }

    @Override
    @Transactional
    public void cancelItem(long itemId) {

        ItemEntity item = itemRepository.findByIdWithFieldsFetched(itemId).orElseThrow(() -> {

            log.error("Cancelling of item with id {} failed. Item not found", itemId);

            return new ItemNotFoundRestException(itemId);
        });

        checkItemEditability(item);
        checkLoggedPersonPermissionToItem(item);

        item.setState(ItemState.CANCELLED);
    }

    private void checkLoggedPersonPermissionToItem(@NonNull ItemEntity item) {

        LoggedUserDetails loggedUser = SecurityUtils.getLoggedUser().orElseThrow(() -> {
            log.error("Update of item with id {} failed. Logged person not found", item.getId());

            return new BaseForbiddenRestException();
        });

        Long sellerId = Optional.of(item).map(ItemEntity::getSeller).map(AbstractIdBasedEntity::getId).orElse(null);
        if (!loggedUser.getId().equals(sellerId)) {
            log.error("Update of item with id {} failed. User with id {} has not permission to update it",
                    item.getId(), loggedUser.getId());

            throw new BaseForbiddenRestException();
        }
    }

    private void checkItemEditability(@NonNull ItemEntity item) {

        if (item.getState() != ItemState.ACTIVE) {
            throw new ItemInNotEditableStateRestException();
        }
    }
}
