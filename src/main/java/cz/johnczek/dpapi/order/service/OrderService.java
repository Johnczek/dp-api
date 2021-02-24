package cz.johnczek.dpapi.order.service;

import cz.johnczek.dpapi.order.dto.OrderDto;
import cz.johnczek.dpapi.order.response.LoggedUserOrdersReponse;
import cz.johnczek.dpapi.order.response.OrderCreationResponse;

import java.util.Optional;

public interface OrderService {

    OrderCreationResponse createOrder(long itemId);

    Optional<OrderDto> getOrder(long orderId);

    LoggedUserOrdersReponse getAllLoggedUserOrders();
}
