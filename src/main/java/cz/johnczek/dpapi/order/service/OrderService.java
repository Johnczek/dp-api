package cz.johnczek.dpapi.order.service;

import cz.johnczek.dpapi.core.errorhandling.exception.BaseForbiddenRestException;
import cz.johnczek.dpapi.core.errorhandling.exception.ItemNotBuyableRestException;
import cz.johnczek.dpapi.core.errorhandling.exception.ItemNotFoundRestException;
import cz.johnczek.dpapi.core.errorhandling.exception.UserNotFoundRestException;
import cz.johnczek.dpapi.order.dto.OrderDto;
import cz.johnczek.dpapi.order.response.LoggedUserOrdersReponse;
import cz.johnczek.dpapi.order.response.OrderCreationResponse;
import lombok.NonNull;

import java.util.List;
import java.util.Optional;

public interface OrderService {

    /**
     * Method creates order for given item
     *
     * @param itemId id of item we want to create order for
     * @return object holding information about created order
     * @throws BaseForbiddenRestException in case that user is not logged in
     * or logged user has no right to create order from given item
     * @throws UserNotFoundRestException if logged user could not be found in database
     * @throws ItemNotFoundRestException in case that item could not be found in database
     * @throws ItemNotBuyableRestException in case we ware unable to find highest bid for given item
     */
    OrderCreationResponse createOrder(long itemId);

    /**
     * @param orderId id of order we want to find
     * @return order with given id
     */
    Optional<OrderDto> getOrder(long orderId);

    /**
     * @param orderIds ids of orders we want to retrieve
     * @return orders by given ids
     */
    List<OrderDto> getOrdersByIds(@NonNull List<Long> orderIds);

    /**
     * @return all logged user orders
     * @throws BaseForbiddenRestException in case that user is not logged in
     */
    LoggedUserOrdersReponse getAllLoggedUserOrders();
}
