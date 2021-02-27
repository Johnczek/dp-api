package cz.johnczek.dpapi.cart.service;

import cz.johnczek.dpapi.cart.response.CartItemResponse;
import cz.johnczek.dpapi.cart.response.CartResponse;

public interface CartService {

    CartResponse getCartForLoggedUser();

    CartItemResponse getCartItemById(long itemId);
}
