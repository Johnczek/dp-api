package cz.johnczek.dpapi.cart.service;

import cz.johnczek.dpapi.cart.response.CartItemResponse;
import cz.johnczek.dpapi.cart.response.CartResponse;
import cz.johnczek.dpapi.core.errorhandling.exception.BaseForbiddenRestException;
import cz.johnczek.dpapi.core.errorhandling.exception.ItemNotBuyableRestException;
import cz.johnczek.dpapi.core.errorhandling.exception.ItemNotFoundRestException;
import cz.johnczek.dpapi.core.security.SecurityUtils;
import cz.johnczek.dpapi.item.dto.ItemDto;
import cz.johnczek.dpapi.item.service.ItemBidService;
import cz.johnczek.dpapi.item.service.ItemService;
import cz.johnczek.dpapi.user.dto.LoggedUserDetails;
import cz.johnczek.dpapi.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final ItemService itemService;

    private final ItemBidService itemBidService;

    private final UserService userService;

    @Override
    @Transactional(readOnly = true)
    public CartResponse getCartForLoggedUser() {

        LoggedUserDetails loggedUser = SecurityUtils.getLoggedUser().orElseThrow(() -> {
            log.error("Getting basket failed. Logged person not found");

            return new BaseForbiddenRestException();
        });

        return CartResponse.builder()
                .userId(loggedUser.getId())
                .cartItems(itemService.findCartItemsForUser(loggedUser.getId()))
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public CartItemResponse getCartItemById(long itemId) {
        itemService.checkItemBuyability(itemId);

        ItemDto itemDto = itemService.findByItemId(itemId).orElseThrow(() -> {
            log.error("Getting basket item failed. Item with id {} not found", itemId);

            return new ItemNotFoundRestException(itemId);
        });

        return CartItemResponse.builder()
                .cartItem(itemDto)
                .build();
    }
}
