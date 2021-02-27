package cz.johnczek.dpapi.cart.service;

import cz.johnczek.dpapi.cart.response.CartItemResponse;
import cz.johnczek.dpapi.cart.response.CartResponse;
import cz.johnczek.dpapi.core.errorhandling.exception.BaseForbiddenRestException;
import cz.johnczek.dpapi.core.errorhandling.exception.ItemNotFoundRestException;

public interface CartService {

    /**
     * @return object holding all orders for logged user
     * @throws BaseForbiddenRestException in case logged person is not logged in
     */
    CartResponse getCartForLoggedUser();

    /**
     * @param itemId id of item we want to find cart item for
     * @return object holding information about given cart item
     * @throws ItemNotFoundRestException in case that item with given id could not be found
     */
    CartItemResponse getCartItemById(long itemId);
}
