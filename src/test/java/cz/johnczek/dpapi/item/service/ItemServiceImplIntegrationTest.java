package cz.johnczek.dpapi.item.service;

import cz.johnczek.dpapi.AbstractIntegrationTest;
import cz.johnczek.dpapi.core.errorhandling.exception.BaseForbiddenRestException;
import cz.johnczek.dpapi.core.errorhandling.exception.DeliveryNotFoundRestException;
import cz.johnczek.dpapi.core.errorhandling.exception.FileNotFoundRestException;
import cz.johnczek.dpapi.core.errorhandling.exception.ItemInNotEditableStateRestException;
import cz.johnczek.dpapi.core.errorhandling.exception.ItemIsNotActiveException;
import cz.johnczek.dpapi.core.errorhandling.exception.ItemNotBuyableRestException;
import cz.johnczek.dpapi.core.errorhandling.exception.ItemNotFoundRestException;
import cz.johnczek.dpapi.core.errorhandling.exception.NotEnoughAmountBidException;
import cz.johnczek.dpapi.core.errorhandling.exception.PaymentNotFoundRestException;
import cz.johnczek.dpapi.core.errorhandling.exception.UserAlreadyHasHighestBidException;
import cz.johnczek.dpapi.delivery.entity.DeliveryEntity;
import cz.johnczek.dpapi.file.entity.FileEntity;
import cz.johnczek.dpapi.item.dto.ItemDto;
import cz.johnczek.dpapi.item.entity.ItemEntity;
import cz.johnczek.dpapi.item.enums.ItemState;
import cz.johnczek.dpapi.item.repository.ItemBidRepository;
import cz.johnczek.dpapi.item.repository.ItemRepository;
import cz.johnczek.dpapi.item.request.ItemChangeDeliveryRequest;
import cz.johnczek.dpapi.item.request.ItemChangePaymentRequest;
import cz.johnczek.dpapi.item.request.ItemChangePictureRequest;
import cz.johnczek.dpapi.item.request.ItemCreationRequest;
import cz.johnczek.dpapi.item.request.ItemWsBidRequest;
import cz.johnczek.dpapi.item.response.ItemCreationOptionsResponse;
import cz.johnczek.dpapi.item.response.ItemEditOptionsResponse;
import cz.johnczek.dpapi.item.response.ItemWsInfoResponse;
import cz.johnczek.dpapi.payment.entity.PaymentEntity;
import cz.johnczek.dpapi.user.response.JwtResponse;
import cz.johnczek.dpapi.user.service.UserService;
import io.jsonwebtoken.MalformedJwtException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ItemServiceImplIntegrationTest extends AbstractIntegrationTest {

    private static final String DUMMY_ITEM_NAME = "Dummy name";
    private static final BigDecimal DUMMY_ITEM_STARTING_PRICE = BigDecimal.ONE;
    private static final LocalDateTime DUMMY_ITEM_VALID_FROM = LocalDateTime.now();
    private static final LocalDateTime DUMMY_ITEM_VALID_TO = LocalDateTime.now().plusDays(10);
    private static final String DUMMY_ITEM_DESCRIPTION = "Lorem ipsum....";
    private static final String DUMMY_FILE_IDENTIFIER = "ba56d4b0-58d8-11eb-ae93-0242ac130002";
    private static final long DUMMY_DELIVERY_ID = 1L;
    private static final long DUMMY_PAYMENT_ID = 1L;

    @Autowired
    private ItemServiceImpl instance;

    @Autowired
    private UserService userService;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ItemBidRepository itemBidRepository;

    @Nested
    class FindAllActive {

        @Test
        void none_activeItemsReturned() {

            List<ItemDto> result = instance.findAllActive();

            assertThat(result).hasSize(4);
        }
    }

    @Nested
    class FindByItemId {

        @Test
        void invalidItemId_emptyOptional() {

            Optional<ItemDto> result = instance.findByItemId(10000L);

            assertThat(result).isEmpty();
        }

        @Test
        void validItemId_itemReturned() {

            long itemId = 1L;
            ItemDto result = instance.findByItemId(itemId).orElse(null);

            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(itemId);
        }
    }

    @Nested
    class FindEntityById {

        @Test
        void invalidItemId_emptyOptional() {

            Optional<ItemEntity> result = instance.findEntityById(10000L);

            assertThat(result).isEmpty();
        }

        @Test
        void validItemId_itemReturned() {

            long itemId = 1L;
            ItemEntity result = instance.findEntityById(itemId).orElse(null);

            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(itemId);
        }
    }

    @Nested
    class FindByItemIdForEdit {

        @Test
        void invalidItemId_exceptionThrown() {

            assertThrows(ItemNotFoundRestException.class,
                    () -> instance.findByItemIdForEdit(10000L)
            );
        }

        @Test
        void validItemId_objectReturned() {

            long itemId = 1L;
            ItemEditOptionsResponse result = instance.findByItemIdForEdit(itemId);

            assertThat(result).isNotNull();
            assertThat(result.getItem()).isNotNull();
            assertThat(result.getItem().getId()).isEqualTo(itemId);
            assertThat(result.getDeliveries()).isNotEmpty();
            assertThat(result.getPayments()).isNotEmpty();
        }
    }

    @Nested
    class FindByItemIds {

        @Test
        void noInput_emptyList() {

            List<ItemDto> result = instance.findByItemIds(Collections.emptySet());

            assertThat(result).isEmpty();
        }

        @Test
        void oneValidId_listOfOneItem() {

            long itemId = 1L;
            List<ItemDto> result = instance.findByItemIds(Set.of(itemId));

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getId()).isEqualTo(itemId);
        }

        @Test
        void oneInvalidId_emptyList() {

            List<ItemDto> result = instance.findByItemIds(Set.of(1000L));

            assertThat(result).isEmpty();
        }

        @Test
        void oneValidAndOneInvalidId_listOfOneItem() {

            long validItemId = 1L;
            long invalidItemId = 1000001L;
            List<ItemDto> result = instance.findByItemIds(Set.of(validItemId, invalidItemId));

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getId()).isEqualTo(validItemId);
        }
    }

    @Nested
    class FindByItemIdsMap {

        @Test
        void noInput_emptyMap() {

            Map<Long, ItemDto> result = instance.findByItemIdsMap(Collections.emptySet());

            assertThat(result).isEmpty();
        }

        @Test
        void oneValidId_mapOfOneItem() {

            long itemId = 1L;
            Map<Long, ItemDto> result = instance.findByItemIdsMap(Set.of(itemId));

            assertThat(result).hasSize(1);
            assertThat(result.get(itemId)).isNotNull();
            assertThat(result.get(itemId).getId()).isEqualTo(itemId);
        }

        @Test
        void oneInvalidId_emptyMap() {

            Map<Long, ItemDto> result = instance.findByItemIdsMap(Set.of(1000L));

            assertThat(result).isEmpty();
        }

        @Test
        void oneValidAndOneInvalidId_mapOfOneItem() {

            long validItemId = 1L;
            long invalidItemId = 1000001L;
            Map<Long, ItemDto> result = instance.findByItemIdsMap(Set.of(validItemId, invalidItemId));

            assertThat(result).hasSize(1);
            assertThat(result.get(validItemId)).isNotNull();
            assertThat(result.get(validItemId).getId()).isEqualTo(validItemId);
        }
    }

    @Nested
    class FindBySellerId {


        @Test
        void invalidSellerId_emptyList() {

            List<ItemDto> result = instance.findBySellerId(1000L);

            assertThat(result).isEmpty();
        }

        @Test
        void sellerWithNoItems_emptyList() {

            List<ItemDto> result = instance.findBySellerId(6L);

            assertThat(result).isEmpty();
        }

        @Test
        void sellerWithItem_listWithItem() {

            long sellerId = 3L;
            List<ItemDto> result = instance.findBySellerId(sellerId);

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getSeller().getId()).isEqualTo(sellerId);
        }
    }

    @Nested
    class ChangeItemDelivery {

        @Test
        void itemNotFound_exceptionThrown() {

            long itemId = 1000L;
            ItemChangeDeliveryRequest request = new ItemChangeDeliveryRequest();
            request.setDeliveryId(1L);

            assertThrows(ItemNotFoundRestException.class,
                    () -> instance.changeItemDelivery(itemId, request)
            );
        }

        @Test
        void notActiveItem_exceptionThrown() {

            long itemId = 1L;
            ItemChangeDeliveryRequest request = new ItemChangeDeliveryRequest();
            request.setDeliveryId(1L);

            ItemEntity item = itemRepository.findById(itemId).orElse(null);
            assertThat(item).isNotNull();
            item.setState(ItemState.AUCTIONED);
            itemRepository.save(item);

            assertThrows(ItemInNotEditableStateRestException.class,
                    () -> instance.changeItemDelivery(itemId, request)
            );

            // cleanup
            item.setState(ItemState.ACTIVE);
            itemRepository.save(item);
        }

        @Test
        void notLoggedUser_exceptionThrown() {

            long itemId = 1L;
            ItemChangeDeliveryRequest request = new ItemChangeDeliveryRequest();
            request.setDeliveryId(1L);

            assertThrows(BaseForbiddenRestException.class,
                    () -> instance.changeItemDelivery(itemId, request)
            );
        }

        @Test
        void loggedUserWithNoRightsToItem_exceptionThrown() {

            long itemId = 2L;
            ItemChangeDeliveryRequest request = new ItemChangeDeliveryRequest();
            request.setDeliveryId(1L);

            String userEmail = "user2@user.com";
            String userPassword = "user";
            userService.login(userEmail, userPassword);

            assertThrows(BaseForbiddenRestException.class,
                    () -> instance.changeItemDelivery(itemId, request)
            );
        }

        @Test
        void validDeliveryId_deliveryChanged() {

            long itemId = 2L;
            ItemChangeDeliveryRequest request = new ItemChangeDeliveryRequest();
            request.setDeliveryId(2L);

            String userEmail = "user@user.com";
            String userPassword = "user";
            userService.login(userEmail, userPassword);

            ItemEntity originalItem = itemRepository.findById(itemId).orElse(null);
            assertThat(originalItem).isNotNull();

            DeliveryEntity originalDelivery = originalItem.getDelivery();

            instance.changeItemDelivery(itemId, request);

            assertThat(originalItem.getDelivery()).isNotNull();
            assertThat(originalItem.getDelivery().getId()).isNotEqualTo(originalDelivery.getId());

            // cleanup
            originalItem.setDelivery(originalDelivery);
            itemRepository.save(originalItem);
        }
    }

    @Nested
    class ChangeItemPaymentMethod {

        @Test
        void itemNotFound_exceptionThrown() {

            long itemId = 1000L;
            ItemChangePaymentRequest request = new ItemChangePaymentRequest();
            request.setPaymentId(1L);

            assertThrows(ItemNotFoundRestException.class,
                    () -> instance.changeItemPaymentMethod(itemId, request)
            );
        }

        @Test
        void notActiveItem_exceptionThrown() {

            long itemId = 1L;
            ItemChangePaymentRequest request = new ItemChangePaymentRequest();
            request.setPaymentId(1L);

            ItemEntity item = itemRepository.findById(itemId).orElse(null);
            assertThat(item).isNotNull();
            item.setState(ItemState.AUCTIONED);
            itemRepository.save(item);

            assertThrows(ItemInNotEditableStateRestException.class,
                    () -> instance.changeItemPaymentMethod(itemId, request)
            );

            // cleanup
            item.setState(ItemState.ACTIVE);
            itemRepository.save(item);
        }

        @Test
        void notLoggedUser_exceptionThrown() {

            long itemId = 1L;
            ItemChangePaymentRequest request = new ItemChangePaymentRequest();
            request.setPaymentId(1L);

            assertThrows(BaseForbiddenRestException.class,
                    () -> instance.changeItemPaymentMethod(itemId, request)
            );
        }

        @Test
        void loggedUserWithNoRightsToItem_exceptionThrown() {

            long itemId = 2L;
            ItemChangePaymentRequest request = new ItemChangePaymentRequest();
            request.setPaymentId(1L);

            String userEmail = "user2@user.com";
            String userPassword = "user";
            userService.login(userEmail, userPassword);

            assertThrows(BaseForbiddenRestException.class,
                    () -> instance.changeItemPaymentMethod(itemId, request)
            );
        }

        @Test
        void validPaymentId_paymentChanged() {

            long itemId = 2L;
            ItemChangePaymentRequest request = new ItemChangePaymentRequest();
            request.setPaymentId(1L);

            String userEmail = "user@user.com";
            String userPassword = "user";
            userService.login(userEmail, userPassword);

            ItemEntity originalItem = itemRepository.findById(itemId).orElse(null);
            assertThat(originalItem).isNotNull();

            PaymentEntity originalPayment = originalItem.getPayment();

            instance.changeItemPaymentMethod(itemId, request);

            assertThat(originalItem.getPayment()).isNotNull();
            assertThat(originalItem.getPayment().getId()).isNotEqualTo(originalPayment.getId());

            // cleanup
            originalItem.setPayment(originalPayment);
            itemRepository.save(originalItem);
        }
    }


    @Nested
    class ChangePicture {

        @Test
        void itemNotFound_exceptionThrown() {

            long itemId = 1000L;
            ItemChangePictureRequest request = new ItemChangePictureRequest();

            assertThrows(ItemNotFoundRestException.class,
                    () -> instance.changePicture(itemId, request)
            );
        }

        @Test
        void notLoggedUser_exceptionThrown() {

            long itemId = 1L;
            ItemChangePictureRequest request = new ItemChangePictureRequest();

            assertThrows(BaseForbiddenRestException.class,
                    () -> instance.changePicture(itemId, request)
            );
        }

        @Test
        void notActiveItem_exceptionThrown() {

            long itemId = 1L;
            ItemChangePictureRequest request = new ItemChangePictureRequest();

            ItemEntity item = itemRepository.findById(itemId).orElse(null);
            assertThat(item).isNotNull();
            item.setState(ItemState.AUCTIONED);
            itemRepository.save(item);

            assertThrows(ItemInNotEditableStateRestException.class,
                    () -> instance.changePicture(itemId, request)
            );

            // cleanup
            item.setState(ItemState.ACTIVE);
            itemRepository.save(item);
        }

        @Test
        void loggedUserWithNoRightsToItem_exceptionThrown() {

            long itemId = 1L;
            ItemChangePictureRequest request = new ItemChangePictureRequest();

            String userEmail = "user2@user.com";
            String userPassword = "user";
            userService.login(userEmail, userPassword);

            assertThrows(BaseForbiddenRestException.class,
                    () -> instance.changePicture(itemId, request)
            );
        }

        @Test
        void invalidUUID_exceptionThrown() {

            long itemId = 1L;
            ItemChangePictureRequest request = new ItemChangePictureRequest();
            request.setPictureUUID("UUID AAA");

            String userEmail = "user@user.com";
            String userPassword = "user";
            userService.login(userEmail, userPassword);

            assertThrows(FileNotFoundRestException.class,
                    () -> instance.changePicture(itemId, request)
            );
        }

        @Test
        void validRequest_pictureChanged() {

            long itemId = 1L;
            String pictureUUID = "9f4ba262-58de-11eb-ae93-0242ac130002";

            ItemChangePictureRequest request = new ItemChangePictureRequest();
            request.setPictureUUID(pictureUUID);

            ItemEntity item = itemRepository.findById(itemId).orElse(null);
            assertThat(item).isNotNull();
            FileEntity originalPicture = item.getPicture();
            assertThat(originalPicture).isNotNull();

            String userEmail = "user@user.com";
            String userPassword = "user";
            userService.login(userEmail, userPassword);

            instance.changePicture(itemId, request);

            assertThat(item.getPicture()).isNotNull();
            assertThat(item.getPicture().getFileIdentifier()).isEqualTo(pictureUUID);

            // cleanup
            item.setPicture(originalPicture);
            itemRepository.save(item);
        }
    }

    @Nested
    class CancelItem {

        @Test
        void invalidItemId_exceptionThrown() {

            long itemId = 10000L;
            assertThrows(ItemNotFoundRestException.class,
                    () -> instance.cancelItem(itemId)
            );
        }

        @Test
        void notActiveItem_exceptionThrown() {

            long itemId = 1L;

            ItemEntity item = itemRepository.findById(itemId).orElse(null);
            assertThat(item).isNotNull();
            item.setState(ItemState.AUCTIONED);
            itemRepository.save(item);

            assertThrows(ItemInNotEditableStateRestException.class,
                    () -> instance.cancelItem(itemId)
            );

            // cleanup
            item.setState(ItemState.ACTIVE);
            itemRepository.save(item);
        }

        @Test
        void notLoggedUser_exceptionThrown() {

            long itemId = 1L;

            assertThrows(BaseForbiddenRestException.class,
                    () -> instance.cancelItem(itemId)
            );
        }

        @Test
        void loggedUserWithNoRightsToUpdateItem_exceptionThrown() {

            long itemId = 1L;
            String userEmail = "user2@user.com";
            String userPassword = "user";
            userService.login(userEmail, userPassword);

            assertThrows(BaseForbiddenRestException.class,
                    () -> instance.cancelItem(itemId)
            );
        }

        @Test
        void validState_itemCancelled() {

            long itemId = 1L;
            String userEmail = "user@user.com";
            String userPassword = "user";
            userService.login(userEmail, userPassword);

            ItemEntity item = itemRepository.findById(itemId).orElse(null);
            assertThat(item).isNotNull();

            instance.cancelItem(itemId);

            assertThat(item.getState()).isEqualTo(ItemState.CANCELLED);

            // cleanup
            item.setState(ItemState.ACTIVE);
            itemRepository.save(item);
        }
    }

    @Nested
    class GetItemCreationOptions {

        @Test
        void none_notEmptyPossibilities() {

            ItemCreationOptionsResponse result = instance.getItemCreationOptions();

            assertAll(
                    () -> assertThat(result).isNotNull(),
                    () -> assertThat(result.getDeliveries()).isNotEmpty(),
                    () -> assertThat(result.getPayments()).isNotEmpty()
            );
        }
    }

    @Nested
    class CreateItem {

        @Test
        void notLoggedUser_exceptionThrown() {

            ItemCreationRequest request = new ItemCreationRequest();

            assertThrows(BaseForbiddenRestException.class,
                    () -> instance.createItem(request)
            );
        }

        @Test
        void paymentNotFound_exceptionThrown() {

            ItemCreationRequest request = new ItemCreationRequest();
            request.setPaymentId(100L);

            String userEmail = "user@user.com";
            String userPassword = "user";
            userService.login(userEmail, userPassword);

            assertThrows(PaymentNotFoundRestException.class,
                    () -> instance.createItem(request)
            );
        }

        @Test
        void deliveryNotFound_exceptionThrown() {

            ItemCreationRequest request = new ItemCreationRequest();
            request.setPaymentId(1L);
            request.setDeliveryId(100L);

            String userEmail = "user@user.com";
            String userPassword = "user";
            userService.login(userEmail, userPassword);

            assertThrows(DeliveryNotFoundRestException.class,
                    () -> instance.createItem(request)
            );
        }

        @Test
        void pictureNotFound_exceptionThrown() {

            ItemCreationRequest request = new ItemCreationRequest();
            request.setPaymentId(1L);
            request.setDeliveryId(1L);
            request.setPictureUUID("DummyUUIDDDDD");

            String userEmail = "user@user.com";
            String userPassword = "user";
            userService.login(userEmail, userPassword);

            assertThrows(FileNotFoundRestException.class,
                    () -> instance.createItem(request)
            );
        }

        @Test
        void validRequest_itemCreated() {

            ItemCreationRequest request = new ItemCreationRequest();
            request.setName(DUMMY_ITEM_NAME);
            request.setDescription(DUMMY_ITEM_DESCRIPTION);
            request.setValidFrom(DUMMY_ITEM_VALID_FROM);
            request.setValidTo(DUMMY_ITEM_VALID_TO);
            request.setStartingPrice(DUMMY_ITEM_STARTING_PRICE.longValue());
            request.setDeliveryId(DUMMY_DELIVERY_ID);
            request.setPaymentId(DUMMY_PAYMENT_ID);
            request.setPictureUUID(DUMMY_FILE_IDENTIFIER);

            String userEmail = "user@user.com";
            String userPassword = "user";
            userService.login(userEmail, userPassword);

            ItemDto result = instance.createItem(request).orElse(null);
            assertThat(result).isNotNull();

            assertAll(
                    () -> assertThat(result.getName()).isEqualTo(DUMMY_ITEM_NAME),
                    () -> assertThat(result.getDescription()).isEqualTo(DUMMY_ITEM_DESCRIPTION),
                    () -> assertThat(result.getValidFrom()).isEqualTo(DUMMY_ITEM_VALID_FROM),
                    () -> assertThat(result.getValidTo()).isEqualTo(DUMMY_ITEM_VALID_TO),
                    () -> assertThat(result.getStartingPrice()).isEqualTo(DUMMY_ITEM_STARTING_PRICE),
                    () -> assertThat(result.getDelivery()).isNotNull(),
                    () -> assertThat(result.getDelivery().getId()).isEqualTo(DUMMY_DELIVERY_ID),
                    () -> assertThat(result.getPayment()).isNotNull(),
                    () -> assertThat(result.getPayment().getId()).isEqualTo(DUMMY_PAYMENT_ID),
                    () -> assertThat(result.getPictureUUID()).isEqualTo(DUMMY_FILE_IDENTIFIER)
            );

            // cleanup
            itemRepository.deleteById(result.getId());
        }
    }

    @Nested
    class FindCartItemsForUser {

        @Test
        void invalidUserId_emptyList() {

            List<ItemDto> result = instance.findCartItemsForUser(100L);

            assertThat(result).isEmpty();
        }

        @Test
        void validUserWithNoWonAuctions_emptyList() {

            List<ItemDto> result = instance.findCartItemsForUser(2L);

            assertThat(result).isEmpty();
        }

        @Test
        void validUserWithWonAuction_listWithOneItem() {

            long userId = 5L;
            long itemId = 1L;
            ItemEntity item = itemRepository.findById(itemId).orElse(null);
            assertThat(item).isNotNull();
            item.setState(ItemState.AUCTIONED);
            itemRepository.save(item);

            List<ItemDto> result = instance.findCartItemsForUser(userId);

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getId()).isEqualTo(itemId);

            // cleanup
            item.setState(ItemState.ACTIVE);
            itemRepository.save(item);
        }
    }

    @Nested
    class CheckItemBuyability {

        @Test
        void noLoggedUser_exceptionThrown() {

            long itemId = 1L;
            assertThrows(BaseForbiddenRestException.class,
                    () -> instance.checkItemBuyability(itemId)
            );
        }

        @Test
        void itemWithNoBids_exceptionThrown() {

            long itemId = 4L;

            String userEmail = "user@user.com";
            String userPassword = "user";
            userService.login(userEmail, userPassword);

            assertThrows(ItemNotBuyableRestException.class,
                    () -> instance.checkItemBuyability(itemId)
            );
        }

        @Test
        void loggedUserNotOwningHighestBid_exceptionThrown() {

            long itemId = 1L;

            String userEmail = "user@user.com";
            String userPassword = "user";
            userService.login(userEmail, userPassword);

            assertThrows(BaseForbiddenRestException.class,
                    () -> instance.checkItemBuyability(itemId)
            );
        }
    }

    @Nested
    class Bid {

        @Test
        void invalidJwtTokenInRequest_exceptionThrown() {

            LocalDateTime currentTime = LocalDateTime.now();
            long itemId = 1L;
            ItemWsBidRequest request = new ItemWsBidRequest();
            request.setItemId(itemId);
            request.setUserJwtToken("InvalidToken");

            assertThrows(MalformedJwtException.class,
                    () -> instance.bid(request, currentTime)
            );
        }

        @Test
        void invalidItemId_exceptionThrown() {

            LocalDateTime currentTime = LocalDateTime.now();
            long itemId = 1000L;
            ItemWsBidRequest request = new ItemWsBidRequest();

            String userEmail = "user@user.com";
            String userPassword = "user";
            JwtResponse loggedUser = userService.login(userEmail, userPassword);

            request.setItemId(itemId);
            request.setUserJwtToken(loggedUser.getToken());

            assertThrows(ItemNotFoundRestException.class,
                    () -> instance.bid(request, currentTime)
            );
        }

        @Test
        void itemNotActive_exceptionThrown() {

            LocalDateTime currentTime = LocalDateTime.now();
            long itemId = 1L;
            ItemWsBidRequest request = new ItemWsBidRequest();

            ItemEntity item = itemRepository.findById(itemId).orElse(null);
            assertThat(item).isNotNull();
            item.setState(ItemState.AUCTIONED);
            itemRepository.save(item);

            String userEmail = "user@user.com";
            String userPassword = "user";
            JwtResponse loggedUser = userService.login(userEmail, userPassword);

            request.setItemId(itemId);
            request.setUserJwtToken(loggedUser.getToken());

            assertThrows(ItemIsNotActiveException.class,
                    () -> instance.bid(request, currentTime)
            );

            // cleanup
            item.setState(ItemState.ACTIVE);
            itemRepository.save(item);
        }

        @Test
        void itemAlreadyExpired_exceptionThrown() {

            LocalDateTime currentTime = LocalDateTime.now();
            long itemId = 1L;
            ItemWsBidRequest request = new ItemWsBidRequest();

            ItemEntity item = itemRepository.findById(itemId).orElse(null);
            assertThat(item).isNotNull();
            LocalDateTime originalValidTo = item.getValidTo();
            item.setValidTo(LocalDateTime.now().minusMinutes(100));
            itemRepository.save(item);

            String userEmail = "user@user.com";
            String userPassword = "user";
            JwtResponse loggedUser = userService.login(userEmail, userPassword);

            request.setItemId(itemId);
            request.setUserJwtToken(loggedUser.getToken());

            assertThrows(ItemIsNotActiveException.class,
                    () -> instance.bid(request, currentTime)
            );

            // cleanup
            item.setValidTo(originalValidTo);
            itemRepository.save(item);
        }

        @Test
        void itemWithoutBidsAmountLessThanStartingPrice_exceptionThrown() {

            LocalDateTime currentTime = LocalDateTime.now();
            long itemId = 3L;
            ItemWsBidRequest request = new ItemWsBidRequest();

            ItemEntity item = itemRepository.findById(itemId).orElse(null);
            assertThat(item).isNotNull();

            String userEmail = "user@user.com";
            String userPassword = "user";
            JwtResponse loggedUser = userService.login(userEmail, userPassword);

            request.setItemId(itemId);
            request.setUserJwtToken(loggedUser.getToken());
            request.setAmount(BigDecimal.ZERO);

            assertThrows(NotEnoughAmountBidException.class,
                    () -> instance.bid(request, currentTime)
            );
        }

        @Test
        void itemWithoutBidsAmountSameAsStartingPrice_exceptionThrown() {

            LocalDateTime currentTime = LocalDateTime.now();
            long itemId = 3L;
            ItemWsBidRequest request = new ItemWsBidRequest();

            ItemEntity item = itemRepository.findById(itemId).orElse(null);
            assertThat(item).isNotNull();

            String userEmail = "user@user.com";
            String userPassword = "user";
            JwtResponse loggedUser = userService.login(userEmail, userPassword);

            request.setItemId(itemId);
            request.setUserJwtToken(loggedUser.getToken());
            request.setAmount(item.getStartingPrice());

            assertThrows(NotEnoughAmountBidException.class,
                    () -> instance.bid(request, currentTime)
            );
        }

        @Test
        void itemWithBidsAmountLowerThanHighestBid_exceptionThrown() {

            LocalDateTime currentTime = LocalDateTime.now();
            long itemId = 1L;
            ItemWsBidRequest request = new ItemWsBidRequest();

            ItemEntity item = itemRepository.findById(itemId).orElse(null);
            assertThat(item).isNotNull();

            String userEmail = "user@user.com";
            String userPassword = "user";
            JwtResponse loggedUser = userService.login(userEmail, userPassword);

            request.setItemId(itemId);
            request.setUserJwtToken(loggedUser.getToken());
            request.setAmount(BigDecimal.ZERO);

            assertThrows(NotEnoughAmountBidException.class,
                    () -> instance.bid(request, currentTime)
            );
        }

        @Test
        void itemWithBidsAmountSameAsHighestBid_exceptionThrown() {

            LocalDateTime currentTime = LocalDateTime.now();
            long itemId = 1L;
            ItemWsBidRequest request = new ItemWsBidRequest();

            ItemEntity item = itemRepository.findById(itemId).orElse(null);
            assertThat(item).isNotNull();

            String userEmail = "user@user.com";
            String userPassword = "user";
            JwtResponse loggedUser = userService.login(userEmail, userPassword);

            request.setItemId(itemId);
            request.setUserJwtToken(loggedUser.getToken());
            request.setAmount(BigDecimal.valueOf(106L));

            assertThrows(NotEnoughAmountBidException.class,
                    () -> instance.bid(request, currentTime)
            );
        }

        @Test
        void userAlreadyOwningHighestBid_exceptionThrown() {

            LocalDateTime currentTime = LocalDateTime.now();
            long itemId = 1L;
            ItemWsBidRequest request = new ItemWsBidRequest();

            ItemEntity item = itemRepository.findById(itemId).orElse(null);
            assertThat(item).isNotNull();

            String userEmail = "user4@user.com";
            String userPassword = "user";
            JwtResponse loggedUser = userService.login(userEmail, userPassword);

            request.setItemId(itemId);
            request.setUserJwtToken(loggedUser.getToken());
            request.setAmount(BigDecimal.valueOf(1000L));

            assertThrows(UserAlreadyHasHighestBidException.class,
                    () -> instance.bid(request, currentTime)
            );
        }

        @Test
        void validState_bidCreated() {

            LocalDateTime currentTime = LocalDateTime.now();
            long itemId = 2L;
            BigDecimal bidAmount = BigDecimal.valueOf(10000L);
            ItemWsBidRequest request = new ItemWsBidRequest();

            ItemEntity item = itemRepository.findById(itemId).orElse(null);
            assertThat(item).isNotNull();

            String userEmail = "user4@user.com";
            String userPassword = "user";
            JwtResponse loggedUser = userService.login(userEmail, userPassword);

            request.setItemId(itemId);
            request.setUserJwtToken(loggedUser.getToken());
            request.setAmount(bidAmount);

            ItemWsInfoResponse result = instance.bid(request, currentTime);
            assertThat(result).isNotNull();

            assertAll(
                    () -> assertThat(result.getItemId()).isEqualTo(itemId),
                    () -> assertThat(result.getItemHighestBid()).isNotNull(),
                    () -> assertThat(result.getItemHighestBid().getItemId()).isEqualTo(itemId),
                    () -> assertThat(result.getItemHighestBid().getUserId()).isEqualTo(loggedUser.getId()),
                    () -> assertThat(result.getItemHighestBid().getAmount()).isEqualByComparingTo(bidAmount),
                    () -> assertThat(result.getItemHighestBid().getTime()).isEqualTo(currentTime)
            );

            // cleanup
            itemBidRepository.deleteAllByItemId(itemId);
        }
    }

    @Nested
    class CheckAndProcessItemsExpiration {

        @Test
        void noItemsForProcessing_emptyList() {

            List<ItemDto> result = instance.checkAndProcessItemsExpiration();

            assertThat(result).isEmpty();
        }

        @Test
        void oneItemWithoutBidsForProcessing_listWithOneItemWithCorrectState() {

            long itemId = 3L;

            ItemEntity item = itemRepository.findById(itemId).orElse(null);
            assertThat(item).isNotNull();
            assertThat(item.getBids()).isEmpty();
            LocalDateTime originalValidTo = item.getValidTo();

            item.setValidTo(LocalDateTime.now().minusMinutes(15));
            itemRepository.save(item);

            List<ItemDto> result = instance.checkAndProcessItemsExpiration();

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getId()).isEqualTo(itemId);
            assertThat(result.get(0).getState()).isEqualTo(ItemState.AUCTIONED_WITHOUT_BIDS);

            // cleanup
            item.setValidTo(originalValidTo);
            item.setState(ItemState.ACTIVE);
            itemRepository.save(item);
        }

        @Test
        void oneItemWithBidsForProcessing_listWithOneItemWithCorrectState() {

            long itemId = 1L;

            ItemEntity item = itemRepository.findById(itemId).orElse(null);
            assertThat(item).isNotNull();
            assertThat(item.getBids()).isNotEmpty();
            LocalDateTime originalValidTo = item.getValidTo();

            item.setValidTo(LocalDateTime.now().minusMinutes(15));
            itemRepository.save(item);

            List<ItemDto> result = instance.checkAndProcessItemsExpiration();

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getId()).isEqualTo(itemId);
            assertThat(result.get(0).getState()).isEqualTo(ItemState.AUCTIONED);

            // cleanup
            item.setValidTo(originalValidTo);
            item.setState(ItemState.ACTIVE);
            itemRepository.save(item);
        }
    }
}