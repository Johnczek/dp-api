package cz.johnczek.dpapi.order.controller;

import cz.johnczek.dpapi.order.dto.OrderDto;
import cz.johnczek.dpapi.order.response.LoggedUserOrdersReponse;
import cz.johnczek.dpapi.order.response.OrderCreationResponse;
import cz.johnczek.dpapi.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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

    @Operation(summary = "Method finds all ulogged users orders", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200", description = "Dto holding all user orders")
    @ApiResponse(responseCode = "403", description = "In case that user is not logged in")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LoggedUserOrdersReponse> getLoggedUserOrders() {
        return new ResponseEntity<>(orderService.getAllLoggedUserOrders(), HttpStatus.OK);
    }

    @Operation(summary = "Method creates order", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "201", description = "Dto holding id of new order")
    @ApiResponse(responseCode = "400", description = "In case that item is not in state from which it can be created order")
    @ApiResponse(responseCode = "403", description = "In case that user is not logged in or has no right to create order for given item")
    @ApiResponse(responseCode = "404", description = "In case that item was not found")
    @PostMapping(value = "/{itemId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderCreationResponse> createOrder(@Parameter(description="Id of item we want to create order for") @PathVariable("itemId") long itemId) {
        return new ResponseEntity<>(orderService.createOrder(itemId), HttpStatus.CREATED);
    }

    @Operation(summary = "Method finds order by id", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200", description = "Dto holding id of order")
    @ApiResponse(responseCode = "400", description = "In case that order could not be retrieved")
    @ApiResponse(responseCode = "403", description = "In case that user is not logged in or has no right to get this order")
    @GetMapping(value = "/{orderId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderDto> getOrderById(@Parameter(description="Id of order we want to retrieve") @PathVariable("orderId") long orderId) {

        Optional<OrderDto> orderOpt = orderService.getOrder(orderId);

        return orderOpt
                .map(i -> new ResponseEntity<>(i, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }
}
