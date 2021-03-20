package cz.johnczek.dpapi.order.service;

import cz.johnczek.dpapi.AbstractIntegrationTest;
import cz.johnczek.dpapi.core.errorhandling.exception.BaseForbiddenRestException;
import cz.johnczek.dpapi.core.errorhandling.exception.ItemNotBuyableRestException;
import cz.johnczek.dpapi.core.errorhandling.exception.ItemNotFoundRestException;
import cz.johnczek.dpapi.core.errorhandling.exception.UserNotFoundRestException;
import cz.johnczek.dpapi.core.security.SecurityUtils;
import cz.johnczek.dpapi.item.entity.ItemEntity;
import cz.johnczek.dpapi.item.enums.ItemState;
import cz.johnczek.dpapi.item.repository.ItemRepository;
import cz.johnczek.dpapi.order.dto.OrderDto;
import cz.johnczek.dpapi.order.entity.OrderEntity;
import cz.johnczek.dpapi.order.repository.OrderRepository;
import cz.johnczek.dpapi.order.response.LoggedUserOrdersReponse;
import cz.johnczek.dpapi.order.response.OrderCreationResponse;
import cz.johnczek.dpapi.user.dto.LoggedUserDetails;
import cz.johnczek.dpapi.user.entity.UserEntity;
import cz.johnczek.dpapi.user.repository.UserRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OrderServiceImplIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private OrderServiceImpl instance;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

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
    class CreateOrder {

        @Test
        void notLoggedUser_exceptionThrown() {
            securityUtilsMock.when(SecurityUtils::getLoggedUser).thenReturn(Optional.empty());

            assertThrows(BaseForbiddenRestException.class, () -> instance.createOrder(0L));
        }

        @Test
        void notRealUser_exceptionThrown() {
            LoggedUserDetails loggedUser = LoggedUserDetails.builder()
                    .id(1000L)
                    .build();

            securityUtilsMock.when(SecurityUtils::getLoggedUser).thenReturn(Optional.of(loggedUser));

            assertThrows(UserNotFoundRestException.class, () -> instance.createOrder(0L));
        }

        @Test
        void noItemFound_exceptionThrown() {
            LoggedUserDetails loggedUser = LoggedUserDetails.builder()
                    .id(1L)
                    .build();

            securityUtilsMock.when(SecurityUtils::getLoggedUser).thenReturn(Optional.of(loggedUser));

            assertThrows(ItemNotFoundRestException.class, () -> instance.createOrder(10000L));
        }

        @Test
        void itemIsNotBuyable_exceptionThrown() {
            LoggedUserDetails loggedUser = LoggedUserDetails.builder()
                    .id(1L)
                    .build();

            securityUtilsMock.when(SecurityUtils::getLoggedUser).thenReturn(Optional.of(loggedUser));

            assertThrows(ItemNotBuyableRestException.class, () -> instance.createOrder(4L));
        }

        @Test
        void loggedUserHasNotHighestBid_exceptionThrown() {
            LoggedUserDetails loggedUser = LoggedUserDetails.builder()
                    .id(1L)
                    .build();

            securityUtilsMock.when(SecurityUtils::getLoggedUser).thenReturn(Optional.of(loggedUser));

            assertThrows(BaseForbiddenRestException.class, () -> instance.createOrder(1L));
        }

        @Test
        void loggedUserHasNotHighestBid_orderCreated() {

            long itemId = 1L;
            long loggedUserId = 5L;
            LoggedUserDetails loggedUser = LoggedUserDetails.builder()
                    .id(loggedUserId)
                    .build();

            securityUtilsMock.when(SecurityUtils::getLoggedUser).thenReturn(Optional.of(loggedUser));

            OrderCreationResponse result = instance.createOrder(itemId);

            assertThat(result.getOrderId()).isNotNull();

            OrderEntity order = orderRepository.findById(result.getOrderId()).orElse(null);
            assertThat(order).isNotNull();
            assertThat(order.getItem().getState()).isEqualTo(ItemState.SOLD);
            assertThat(order.getItem().getId()).isEqualTo(itemId);
            assertThat(order.getBuyer().getId()).isEqualTo(loggedUserId);

            // cleanup
            orderRepository.delete(order);
        }
    }

    @Nested
    class GetOrder {

        @Test
        void nonExistingOrder_emptyOptional() {

            Optional<OrderDto> orderOpt = instance.getOrder(100L);

            assertThat(orderOpt).isEmpty();
        }

        @Test
        void existingOrderRealOrder_correspondingOrder() {

            UserEntity seller = userRepository.findByUserId(1L).orElse(null);
            UserEntity buyer = userRepository.findByUserId(2L).orElse(null);
            ItemEntity item = itemRepository.findById(1L).orElse(null);

            assertThat(seller).isNotNull();
            assertThat(buyer).isNotNull();
            assertThat(item).isNotNull();

            OrderEntity order = orderRepository.save(prepareDummyOrder(item, seller, buyer));

            Optional<OrderDto> orderOpt = instance.getOrder(order.getId());

            assertThat(orderOpt).isNotEmpty();
            OrderDto result = orderOpt.get();

            assertAll(
                    () -> assertThat(result.getOrderId()).isEqualTo(order.getId()),
                    () -> assertThat(result.getCreated()).isEqualTo(order.getCreated()),
                    () -> assertThat(result.getItem()).isNotNull(),
                    () -> assertThat(result.getItem().getId()).isEqualTo(item.getId()),
                    () -> assertThat(result.getBuyer()).isNotNull(),
                    () -> assertThat(result.getBuyer().getId()).isEqualTo(buyer.getId()),
                    () -> assertThat(result.getSeller()).isNotNull(),
                    () -> assertThat(result.getSeller().getId()).isEqualTo(seller.getId())
            );

            // cleanup
            orderRepository.delete(order);
        }
    }

    @Nested
    class GetOrdersByIds {

        @Test
        void noIds_emptyList() {

            List<OrderDto> result = instance.getOrdersByIds(Collections.emptyList());

            assertThat(result).isEmpty();
        }

        @Test
        void nonExistingOrderIds_emptyList() {

            List<OrderDto> result = instance.getOrdersByIds(List.of(100L, 111L));

            assertThat(result).isEmpty();
        }

        @Test
        void oneExistingOrder_listWithThatOrder() {

            UserEntity seller = userRepository.findByUserId(1L).orElse(null);
            UserEntity buyer = userRepository.findByUserId(2L).orElse(null);
            ItemEntity item = itemRepository.findById(1L).orElse(null);

            assertThat(seller).isNotNull();
            assertThat(buyer).isNotNull();
            assertThat(item).isNotNull();

            OrderEntity order = orderRepository.save(prepareDummyOrder(item, seller, buyer));

            List<OrderDto> result = instance.getOrdersByIds(List.of(order.getId()));

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getOrderId()).isEqualTo(order.getId());

            // cleanup
            orderRepository.delete(order);
        }

        @Test
        void oneExistingOrderAndOneNonExisting_listWithThatOrder() {

            UserEntity seller = userRepository.findByUserId(1L).orElse(null);
            UserEntity buyer = userRepository.findByUserId(2L).orElse(null);
            ItemEntity item = itemRepository.findById(1L).orElse(null);

            assertThat(seller).isNotNull();
            assertThat(buyer).isNotNull();
            assertThat(item).isNotNull();

            OrderEntity order = orderRepository.save(prepareDummyOrder(item, seller, buyer));

            List<OrderDto> result = instance.getOrdersByIds(List.of(order.getId(), 1234L));

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getOrderId()).isEqualTo(order.getId());

            // cleanup
            orderRepository.delete(order);
        }
    }

    @Nested
    class GetAllLoggedUserOrders {

        @Test
        void notLoggedUser_exceptionThrown() {
            securityUtilsMock.when(SecurityUtils::getLoggedUser).thenReturn(Optional.empty());

            assertThrows(BaseForbiddenRestException.class, () -> instance.getAllLoggedUserOrders());
        }

        @Test
        void loggedUserWithNoOrders_emptyOrderList() {

            long loggedUserId = 1L;
            LoggedUserDetails loggedUser = LoggedUserDetails.builder()
                    .id(loggedUserId)
                    .build();

            securityUtilsMock.when(SecurityUtils::getLoggedUser).thenReturn(Optional.of(loggedUser));

            LoggedUserOrdersReponse loggedUserOrders = instance.getAllLoggedUserOrders();

            assertThat(loggedUserOrders).isNotNull();
            assertThat(loggedUserOrders.getOrders()).isNullOrEmpty();
        }

        @Test
        void loggedUserWithOrder_orderInList() {

            long loggedUserId = 2L;
            LoggedUserDetails loggedUser = LoggedUserDetails.builder()
                    .id(loggedUserId)
                    .build();
            securityUtilsMock.when(SecurityUtils::getLoggedUser).thenReturn(Optional.of(loggedUser));

            UserEntity seller = userRepository.findByUserId(1L).orElse(null);
            UserEntity buyer = userRepository.findByUserId(loggedUserId).orElse(null);
            ItemEntity item = itemRepository.findById(1L).orElse(null);

            assertThat(seller).isNotNull();
            assertThat(buyer).isNotNull();
            assertThat(item).isNotNull();

            OrderEntity order = orderRepository.save(prepareDummyOrder(item, seller, buyer));

            LoggedUserOrdersReponse loggedUserOrders = instance.getAllLoggedUserOrders();

            assertThat(loggedUserOrders).isNotNull();
            assertThat(loggedUserOrders.getOrders()).hasSize(1);
            assertThat(loggedUserOrders.getOrders().get(0).getOrderId()).isEqualTo(order.getId());

            // cleanup
            orderRepository.delete(order);
        }
    }

    private OrderEntity prepareDummyOrder(ItemEntity item, UserEntity seller, UserEntity buyer) {

        OrderEntity result = new OrderEntity();
        result.setCreated(LocalDateTime.now());
        result.setItem(item);
        result.setBuyer(buyer);
        result.setSeller(seller);

        return result;
    }
}