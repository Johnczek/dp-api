package cz.johnczek.dpapi.order.controller;

import cz.johnczek.dpapi.order.dto.OrderDto;
import cz.johnczek.dpapi.order.response.LoggedUserOrdersReponse;
import cz.johnczek.dpapi.order.response.OrderCreationResponse;
import cz.johnczek.dpapi.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LoggedUserOrdersReponse> getLoggedUserOrders() {
        return new ResponseEntity<>(orderService.getAllLoggedUserOrders(), HttpStatus.OK);
    }

    @PostMapping(value = "/{itemId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderCreationResponse> createOrder(@PathVariable("itemId") long itemId) {
        return new ResponseEntity<>(orderService.createOrder(itemId), HttpStatus.OK);
    }

    @GetMapping(value = "/{orderId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderDto> getOrderById(@PathVariable("orderId") long orderId) {

        Optional<OrderDto> orderOpt = orderService.getOrder(orderId);

        return orderOpt
                .map(i -> new ResponseEntity<>(i, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }
}
