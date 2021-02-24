package cz.johnczek.dpapi.cart.service;

import cz.johnczek.dpapi.cart.response.CartResponse;

public interface CartService {

    CartResponse getCartByUserId(long userId);
}
