package cz.johnczek.dpapi.order.service;

import cz.johnczek.dpapi.core.errorhandling.exception.BaseForbiddenRestException;
import cz.johnczek.dpapi.core.errorhandling.exception.ItemNotFoundRestException;
import cz.johnczek.dpapi.core.errorhandling.exception.UserNotFoundRestException;
import cz.johnczek.dpapi.core.security.SecurityUtils;
import cz.johnczek.dpapi.item.dto.ItemDto;
import cz.johnczek.dpapi.item.entity.ItemEntity;
import cz.johnczek.dpapi.item.service.ItemService;
import cz.johnczek.dpapi.order.dto.OrderDto;
import cz.johnczek.dpapi.order.entity.OrderEntity;
import cz.johnczek.dpapi.order.mapper.OrderMapper;
import cz.johnczek.dpapi.order.repository.OrderRepository;
import cz.johnczek.dpapi.order.response.LoggedUserOrdersReponse;
import cz.johnczek.dpapi.order.response.OrderCreationResponse;
import cz.johnczek.dpapi.user.dto.LoggedUserDetails;
import cz.johnczek.dpapi.user.dto.UserDto;
import cz.johnczek.dpapi.user.entity.UserEntity;
import cz.johnczek.dpapi.user.service.UserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final UserService userService;

    private final ItemService itemService;

    private final OrderMapper ordermapper;

    @Override
    @Transactional
    public OrderCreationResponse createOrder(long itemId) {

        LoggedUserDetails loggedUser = SecurityUtils.getLoggedUser().orElseThrow(() -> {
            log.error("Creating order failed. No user logged");

            return new BaseForbiddenRestException();
        });

        UserEntity buyer = userService.findEntityById(loggedUser.getId()).orElseThrow(() -> {
            log.error("Creating order failed. Logged user with id {} not found in database", loggedUser.getId());

            return new UserNotFoundRestException(loggedUser.getId());
        });

        ItemEntity item = itemService.findEntityById(itemId).orElseThrow(() -> {
            log.error("Creating order failed. Item with id {} not found", itemId);

            return new ItemNotFoundRestException(itemId);
        });

        OrderEntity order = new OrderEntity();
        order.setCreated(LocalDateTime.now());
        order.setBuyer(buyer);
        order.setSeller(item.getSeller());
        order.setItem(item);
        orderRepository.save(order);

        return OrderCreationResponse.builder()
                .orderId(order.getId())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OrderDto> getOrder(long orderId) {

        List<OrderDto> orders = getOrdersByIds(Collections.singletonList(orderId));
        if (CollectionUtils.isEmpty(orders) || orders.size() != 1) {
            return Optional.empty();
        }

        return Optional.of(orders.get(0));
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDto> getOrdersByIds(@NonNull List<Long> orderIds) {

        if (CollectionUtils.isEmpty(orderIds)) {
            return Collections.emptyList();
        }

        List<OrderEntity> orders = orderRepository.findOrdersByIds(orderIds);
        if (CollectionUtils.isEmpty(orders)) {
            return Collections.emptyList();
        }

        Set<Long> sellerIds = orders.stream()
                .map(o -> o.getSeller().getId()).collect(Collectors.toSet());
        List<Long> buyerIds = orders.stream()
                .map(o -> o.getBuyer().getId()).collect(Collectors.toList());
        sellerIds.addAll(buyerIds);

        List<Long> userIds = new ArrayList<>(sellerIds);

        Map<Long, UserDto> usersMap = userService.findByUserIds(userIds);
        Map<Long, ItemDto> itemsMap = itemService.findByItemIdsMap(new HashSet<>(userIds));

        return orders.stream()
                .map(o -> ordermapper.entityToDto(o,
                        usersMap.get(o.getBuyer().getId()),
                        usersMap.get(o.getSeller().getId()),
                        itemsMap.get(o.getItem().getId())))
                .collect(Collectors.toList());


    }

    @Override
    @Transactional(readOnly = true)
    public LoggedUserOrdersReponse getAllLoggedUserOrders() {

        LoggedUserDetails loggedUser = SecurityUtils.getLoggedUser().orElseThrow(() -> {
            log.error("Getting logged person orders failed. No user logged");

            return new BaseForbiddenRestException();
        });

        List<Long> orderIds = orderRepository.findOrderIdsByBuyerId(loggedUser.getId());
        if (CollectionUtils.isEmpty(orderIds)) {
            return LoggedUserOrdersReponse.builder().build();
        }

        return LoggedUserOrdersReponse.builder()
                .orders(getOrdersByIds(orderIds))
                .build();
    }
}
