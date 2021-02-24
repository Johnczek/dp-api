package cz.johnczek.dpapi.order.service;

import cz.johnczek.dpapi.order.dto.OrderDto;
import cz.johnczek.dpapi.order.response.LoggedUserOrdersReponse;
import cz.johnczek.dpapi.order.response.OrderCreationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    @Override
    @Transactional
    public OrderCreationResponse createOrder(long itemId) {
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OrderDto> getOrder(long orderId) {
        return Optional.empty();
    }

    @Override
    @Transactional(readOnly = true)
    public LoggedUserOrdersReponse getAllLoggedUserOrders() {
        return null;
    }
}
