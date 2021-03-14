package cz.johnczek.dpapi.cart.controller;

import cz.johnczek.dpapi.cart.response.CartItemResponse;
import cz.johnczek.dpapi.cart.response.CartResponse;
import cz.johnczek.dpapi.cart.service.CartService;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    @Operation(summary = "Method returns cart of currently logged user", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200", description = "If cart was found. Reponse holds cart of currently logged person")
    @ApiResponse(responseCode = "403", description = "In case that user is not logged in")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CartResponse> getCartForLoggedUser() {
        return new ResponseEntity<>(cartService.getCartForLoggedUser(), HttpStatus.OK);
    }

    @Operation(summary = "Method returns cart row by item id", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200", description = "If cart was found. Reponse holds cart of currently logged person")
    @ApiResponse(responseCode = "400", description = "In case that item is not in buyable state")
    @ApiResponse(responseCode = "403", description = "In case that user is not logged in or logged user has no right to get this basket item")
    @ApiResponse(responseCode = "404", description = "In case that item was not found")
    @GetMapping(value = "/{itemId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CartItemResponse> getCartItem(@Parameter(description="Id of item we want to get cart row for") @PathVariable("itemId") long itemId) {
        return new ResponseEntity<>(cartService.getCartItemById(itemId), HttpStatus.OK);
    }
}
