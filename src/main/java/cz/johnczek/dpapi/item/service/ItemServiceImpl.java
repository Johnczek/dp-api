package cz.johnczek.dpapi.item.service;

import cz.johnczek.dpapi.delivery.dto.DeliveryDto;
import cz.johnczek.dpapi.delivery.service.DeliveryService;
import cz.johnczek.dpapi.item.dto.ItemDto;
import cz.johnczek.dpapi.item.entity.ItemEntity;
import cz.johnczek.dpapi.item.mapper.ItemMapper;
import cz.johnczek.dpapi.item.repository.ItemRepository;
import cz.johnczek.dpapi.payment.dto.PaymentDto;
import cz.johnczek.dpapi.payment.service.PaymentService;
import cz.johnczek.dpapi.user.dto.UserDto;
import cz.johnczek.dpapi.user.service.UserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
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

    private final ItemMapper itemMapper;

    public List<ItemDto> findByItemIds(@NonNull Set<Long> itemIds) {

        if (CollectionUtils.isEmpty(itemIds)) {
            return Collections.emptyList();
        }

        Map<Long, PaymentDto> paymentsMap = paymentService.findByItemIds(itemIds);
        Map<Long, DeliveryDto> deliveriesMap = deliveryService.findByItemIds(itemIds);
        Map<Long, UserDto> usersMap = userService.findByItemIds(itemIds);

        return itemRepository.findByItemIdsWithFields(itemIds).stream()
                .map(i -> itemMapper.entityToDto(
                        i,
                        deliveriesMap.get(i.getDelivery().getId()),
                        paymentsMap.get(i.getPayment().getId()),
                        usersMap.get(i.getSeller().getId())))
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> findAll() {
        Set<ItemEntity> items = itemRepository.findAllWithFieldsFetched();
        Set<Long> itemIds = items.stream().map(ItemEntity::getId).collect(Collectors.toSet());

        Map<Long, PaymentDto> paymentsMap = paymentService.findByItemIds(itemIds);
        Map<Long, DeliveryDto> deliveriesMap = deliveryService.findByItemIds(itemIds);
        Map<Long, UserDto> usersMap = userService.findByItemIds(itemIds);

        return items.stream()
                .map(i -> itemMapper.entityToDto(
                        i,
                        deliveriesMap.get(i.getDelivery().getId()),
                        paymentsMap.get(i.getPayment().getId()),
                        usersMap.get(i.getSeller().getId())))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ItemDto> findByItemId(@NonNull long itemId) {
        return findByItemIds(Collections.singleton(itemId)).stream().findFirst();
    }
}
