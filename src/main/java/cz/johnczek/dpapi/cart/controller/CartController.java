package cz.johnczek.dpapi.cart.controller;

import cz.johnczek.dpapi.cart.response.CartItemResponse;
import cz.johnczek.dpapi.cart.response.CartResponse;
import cz.johnczek.dpapi.cart.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CartResponse> getCartForLoggedUser() {
        return new ResponseEntity<>(cartService.getCartForLoggedUser(), HttpStatus.OK);
    }

    @GetMapping(value = "/{itemId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CartItemResponse> getCartItem(@PathVariable("itemId") long itemId) {
        return new ResponseEntity<>(cartService.getCartItemById(itemId), HttpStatus.OK);
    }
}
