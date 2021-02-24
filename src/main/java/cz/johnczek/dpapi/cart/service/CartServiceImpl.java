package cz.johnczek.dpapi.cart.service;

import cz.johnczek.dpapi.cart.response.CartResponse;
import cz.johnczek.dpapi.item.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final ItemService itemService;

    @Override
    @Transactional(readOnly = true)
    public CartResponse getCartByUserId(long userId) {

        return CartResponse.builder()
                .userId(userId)
                .cartItems(itemService.findCartItemsForUser(userId))
                .build();
    }
}
