package cz.johnczek.dpapi.cart.service;

import cz.johnczek.dpapi.AbstractIntegrationTest;
import cz.johnczek.dpapi.cart.response.CartItemResponse;
import cz.johnczek.dpapi.cart.response.CartResponse;
import cz.johnczek.dpapi.core.errorhandling.exception.BaseForbiddenRestException;
import cz.johnczek.dpapi.core.errorhandling.exception.ItemNotBuyableRestException;
import cz.johnczek.dpapi.core.errorhandling.exception.ItemNotFoundRestException;
import cz.johnczek.dpapi.core.security.SecurityUtils;
import cz.johnczek.dpapi.item.entity.ItemEntity;
import cz.johnczek.dpapi.item.enums.ItemState;
import cz.johnczek.dpapi.item.repository.ItemRepository;
import cz.johnczek.dpapi.user.dto.LoggedUserDetails;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CartServiceImplIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private CartServiceImpl instance;

    @Autowired
    private ItemRepository itemRepository;

    private static MockedStatic<SecurityUtils> securityUtilsMock;

    @BeforeAll
    static void beforeAll() {

        securityUtilsMock = Mockito.mockStatic(SecurityUtils.class);
    }

    @AfterAll
    static void afterAll() {
        securityUtilsMock.close();
    }

    @Nested
    class GetCartForLoggedUser {

        @Test
        void noLoggedUser_exceptionThrown() {

            securityUtilsMock.when(SecurityUtils::getLoggedUser).thenReturn(Optional.empty());

            assertThrows(BaseForbiddenRestException.class, () -> instance.getCartForLoggedUser());
        }

        @Test
        void loggedUserWithNoItems_emptyCartList() {

            LoggedUserDetails loggedUser = LoggedUserDetails.builder()
                    .id(1L)
                    .build();

            securityUtilsMock.when(SecurityUtils::getLoggedUser).thenReturn(Optional.of(loggedUser));

            CartResponse result = instance.getCartForLoggedUser();

            assertThat(result).isNotNull();
            assertThat(result.getUserId()).isEqualTo(loggedUser.getId());
            assertThat(result.getCartItems()).isNullOrEmpty();
        }

        @Test
        void loggedUserWithItem_cartObjectWithItem() {

            LoggedUserDetails loggedUser = LoggedUserDetails.builder()
                    .id(5L)
                    .build();
            securityUtilsMock.when(SecurityUtils::getLoggedUser).thenReturn(Optional.of(loggedUser));

            ItemEntity item = itemRepository.findById(1L).orElse(null);
            assertThat(item).isNotNull();

            item.setState(ItemState.AUCTIONED);
            itemRepository.save(item);

            CartResponse result = instance.getCartForLoggedUser();

            assertThat(result).isNotNull();
            assertThat(result.getUserId()).isEqualTo(loggedUser.getId());
            assertThat(result.getCartItems()).hasSize(1);
            assertThat(result.getCartItems().get(0).getId()).isEqualTo(item.getId());

            // cleanup
            item.setState(ItemState.ACTIVE);
            itemRepository.save(item);
        }
    }

    @Nested
    class GetCartItemById {

        @Test
        void notLoggedPerson_exceptionThrown() {

            securityUtilsMock.when(SecurityUtils::getLoggedUser).thenReturn(Optional.empty());

            assertThrows(BaseForbiddenRestException.class, () -> instance.getCartItemById(1L));
        }

        @Test
        void itemWithoutBids_exceptionThrown() {

            LoggedUserDetails loggedUser = LoggedUserDetails.builder()
                    .id(5L)
                    .build();
            securityUtilsMock.when(SecurityUtils::getLoggedUser).thenReturn(Optional.of(loggedUser));

            assertThrows(ItemNotBuyableRestException.class, () -> instance.getCartItemById(2L));
        }

        @Test
        void loggedPersonWithoutHighestBid_exceptionThrown() {

            LoggedUserDetails loggedUser = LoggedUserDetails.builder()
                    .id(4L)
                    .build();
            securityUtilsMock.when(SecurityUtils::getLoggedUser).thenReturn(Optional.of(loggedUser));

            assertThrows(BaseForbiddenRestException.class, () -> instance.getCartItemById(1L));
        }

        @Test
        void nonExistingItem_exceptionThrown() {

            LoggedUserDetails loggedUser = LoggedUserDetails.builder()
                    .id(4L)
                    .build();
            securityUtilsMock.when(SecurityUtils::getLoggedUser).thenReturn(Optional.of(loggedUser));

            assertThrows(ItemNotFoundRestException.class, () -> instance.getCartItemById(1000L));
        }



        @Test
        void aloggedPersonWithoutHighestBid_exceptionThrown() {

            long itemId = 1L;
            LoggedUserDetails loggedUser = LoggedUserDetails.builder()
                    .id(5L)
                    .build();
            securityUtilsMock.when(SecurityUtils::getLoggedUser).thenReturn(Optional.of(loggedUser));

            ItemEntity item = itemRepository.findById(itemId).orElse(null);
            assertThat(item).isNotNull();

            item.setState(ItemState.AUCTIONED);
            itemRepository.save(item);


            CartItemResponse result = instance.getCartItemById(itemId);
            assertThat(result).isNotNull();
            assertThat(result.getCartItem()).isNotNull();
            assertThat(result.getCartItem().getId()).isEqualTo(itemId);

            // cleanup
            item.setState(ItemState.ACTIVE);
            itemRepository.save(item);
        }
    }
}