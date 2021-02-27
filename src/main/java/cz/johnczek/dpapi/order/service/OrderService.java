package cz.johnczek.dpapi.order.service;

import cz.johnczek.dpapi.order.dto.OrderDto;
import cz.johnczek.dpapi.order.response.LoggedUserOrdersReponse;
import cz.johnczek.dpapi.order.response.OrderCreationResponse;
import lombok.NonNull;

import java.util.List;
import java.util.Optional;

public interface OrderService {

    OrderCreationResponse createOrder(long itemId);

    Optional<OrderDto> getOrder(long orderId);

    List<OrderDto> getOrdersByIds(@NonNull List<Long> orderIds);

    LoggedUserOrdersReponse getAllLoggedUserOrders();
}
