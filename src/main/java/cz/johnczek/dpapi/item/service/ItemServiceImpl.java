package cz.johnczek.dpapi.item.service;

import cz.johnczek.dpapi.core.errorhandling.exception.BaseForbiddenRestException;
import cz.johnczek.dpapi.core.errorhandling.exception.DeliveryNotFoundRestException;
import cz.johnczek.dpapi.core.errorhandling.exception.ItemNotFoundRestException;
import cz.johnczek.dpapi.core.persistence.AbstractIdBasedEntity;
import cz.johnczek.dpapi.core.security.SecurityUtils;
import cz.johnczek.dpapi.delivery.dto.DeliveryDto;
import cz.johnczek.dpapi.delivery.entity.DeliveryEntity;
import cz.johnczek.dpapi.delivery.service.DeliveryService;
import cz.johnczek.dpapi.item.dto.ItemDto;
import cz.johnczek.dpapi.item.dto.ItemHighestBidDto;
import cz.johnczek.dpapi.item.entity.ItemEntity;
import cz.johnczek.dpapi.item.mapper.ItemMapper;
import cz.johnczek.dpapi.item.repository.ItemRepository;
import cz.johnczek.dpapi.item.request.ItemChangeDeliveryRequest;
import cz.johnczek.dpapi.payment.dto.PaymentDto;
import cz.johnczek.dpapi.payment.service.PaymentService;
import cz.johnczek.dpapi.user.dto.LoggedUserDetails;
import cz.johnczek.dpapi.user.dto.UserDto;
import cz.johnczek.dpapi.user.service.UserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public List<ItemDto> findAll() {
        Set<ItemEntity> items = itemRepository.findAllWithFieldsFetched();
        Set<Long> itemIds = items.stream().map(ItemEntity::getId).collect(Collectors.toSet());

        Map<Long, ItemHighestBidDto> itemHighestBidDtoMap = itemBidService.findHighestBidByItemIds(itemIds);
        Map<Long, PaymentDto> paymentsMap = paymentService.findByItemIds(itemIds);
        Map<Long, DeliveryDto> deliveriesMap = deliveryService.findByItemIds(itemIds);
        Map<Long, UserDto> usersMap = userService.findByItemIds(itemIds);

        return items.stream()
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
    public Optional<ItemDto> findByItemId(@NonNull long itemId) {
        return findByItemIds(Collections.singleton(itemId)).stream().findFirst();
    }

    @Override
    @Transactional
    public void changeItemDelivery(
            long itemId,
            @NonNull ItemChangeDeliveryRequest itemChangeDeliveryRequest) {

        ItemEntity item = itemRepository.findByIdWithFieldsFetched(itemId).orElseThrow(() -> {

            log.error("Update delivery of item with id {} failed. Item not found", itemId);

            return new ItemNotFoundRestException(itemId);
        });

        checkLoggedPersonPermissionToItem(item);

        Long deliveryId = itemChangeDeliveryRequest.getDeliveryId();
        DeliveryEntity delivery = deliveryService.findById(deliveryId).orElseThrow(() -> {

            log.error("Update delivery of item with id {} failed. Delivery with id {} not found",
                    itemId,
                    deliveryId);

            return new DeliveryNotFoundRestException(deliveryId);
        });

        item.setDelivery(delivery);
    }

    private void checkLoggedPersonPermissionToItem(@NonNull ItemEntity itemEntity) {

        LoggedUserDetails loggedUser = SecurityUtils.getLoggedUser().orElseThrow(() -> {
            log.error("Update of item with id {} failed. Logged person not found", itemEntity.getId());

            return new BaseForbiddenRestException();
        });

        Long sellerId = Optional.of(itemEntity).map(ItemEntity::getSeller).map(AbstractIdBasedEntity::getId).orElse(null);
        if (!loggedUser.getId().equals(sellerId)) {
            log.error("Update of item with id {} failed. User with id {} has not permission to update it",
                    itemEntity.getId(), loggedUser.getId());

            throw new BaseForbiddenRestException();
        }
    }
}
