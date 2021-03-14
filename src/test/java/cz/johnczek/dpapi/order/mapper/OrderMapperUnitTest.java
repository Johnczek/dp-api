package cz.johnczek.dpapi.order.mapper;

import cz.johnczek.dpapi.delivery.dto.DeliveryDto;
import cz.johnczek.dpapi.item.dto.ItemDto;
import cz.johnczek.dpapi.item.dto.ItemHighestBidDto;
import cz.johnczek.dpapi.item.enums.ItemState;
import cz.johnczek.dpapi.order.dto.OrderDto;
import cz.johnczek.dpapi.order.entity.OrderEntity;
import cz.johnczek.dpapi.payment.dto.PaymentDto;
import cz.johnczek.dpapi.user.dto.AddressDto;
import cz.johnczek.dpapi.user.dto.BankAccountDto;
import cz.johnczek.dpapi.user.dto.UserDto;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@ExtendWith(MockitoExtension.class)
class OrderMapperUnitTest {

    private static final long DUMMY_ITEM_ID = 1L;
    private static final ItemState DUMMY_ITEM_STATE = ItemState.ACTIVE;
    private static final String DUMMY_ITEM_NAME = "Dummy name";
    private static final BigDecimal DUMMY_ITEM_STARTING_PRICE = BigDecimal.ONE;
    private static final LocalDateTime DUMMY_ITEM_VALID_FROM = LocalDateTime.now();
    private static final LocalDateTime DUMMY_ITEM_VALID_TO = LocalDateTime.now().plusDays(10);
    private static final String DUMMY_ITEM_DESCRIPTION = "Lorem ipsum....";
    private static final long DUMMY_DELIVERY_ID = 2L;
    private static final String DUMMY_DELIVERY_NAME = "Delivery name";
    private static final String DUMMY_DELIVERY_DESCRIPTION = "Delivery description";
    private static final BigDecimal DUMMY_DELIVERY_PRICE = BigDecimal.ONE;
    private static final long DUMMY_PAYMENT_ID = 3L;
    private static final String DUMMY_PAYMENT_NAME = "Payment name";
    private static final String DUMMY_PAYMENT_DESCRIPTION = "Payment description";
    private static final BigDecimal DUMMY_PAYMENT_PRICE = BigDecimal.TEN;
    private static final BigDecimal DUMMY_HIGHEST_BID_AMOUNT = new BigDecimal("1000");
    private static final LocalDateTime DUMMY_HIGHEST_BID_TIME = LocalDateTime.now().minusMinutes(10);
    private static final String DUMMY_FILE_IDENTIFIER = "8a658007-7c3a-4a01-9278-9389f93af3c7";
    private static final long DUMMY_ADDRESS_ID = 1000L;
    private static final String DUMMY_CITY = "City";
    private static final String DUMMY_STREET = "Street";
    private static final String DUMMY_STREET_NUMBER = "StreetNr";
    private static final String DUMMY_ZIPCODE = "12300";
    private static final long DUMMY_BANK_ACCOUNT_ID = 2000L;
    private static final long DUMMY_BANK_ACCOUNT_PREFIX = 100L;
    private static final long DUMMY_BANK_ACCOUNT_NUMBER = 155050;
    private static final int DUMMY_BANK_ACCOUNT_CODE = 1010;
    private static final long DUMMY_BUYER_ID = 4L;
    private static final String DUMMY_BUYER_FIRST_NAME = "Firstname";
    private static final String DUMMY_BUYER_LAST_NAME = "Lastname";
    private static final String DUMMY_BUYER_DESCRIPTION = "User description";
    private static final String DUMMY_BUYER_EMAIL = "buyer@email.com";
    private static final long DUMMY_SELLER_ID = 4L;
    private static final String DUMMY_SELLER_FIRST_NAME = "John";
    private static final String DUMMY_SELLER_LAST_NAME = "Doe";
    private static final String DUMMY_SELLER_DESCRIPTION = "Seller description lorem ipsum";
    private static final String DUMMY_SELLER_EMAIL = "seller@gmail.com";
    private static final LocalDateTime ORDER_CREATED = LocalDateTime.now().minusMinutes(33);
    private static final long ORDER_ID = 666L;

    @InjectMocks
    private OrderMapperImpl instance;

    @Nested
    class EntityToDto {

        @Test
        void nullInput_nullOutput() {

            OrderEntity orderEntity = prepareDummyOrderEntity();
            ItemDto itemDto = prepareDummyItemDto();
            UserDto buyer = prepareDummyUser(DUMMY_BUYER_ID, DUMMY_BUYER_FIRST_NAME, DUMMY_BUYER_LAST_NAME, DUMMY_BUYER_EMAIL, DUMMY_BUYER_DESCRIPTION);
            UserDto seller = prepareDummyUser(DUMMY_SELLER_ID, DUMMY_SELLER_FIRST_NAME, DUMMY_SELLER_LAST_NAME, DUMMY_SELLER_EMAIL, DUMMY_SELLER_DESCRIPTION);

            OrderDto result = instance.entityToDto(
                    orderEntity,
                    buyer,
                    seller,
                    itemDto);

            assertAll(
                    () -> assertThat(result).isNotNull(),
                    () -> assertThat(result.getOrderId()).isEqualTo(ORDER_ID),
                    () -> assertThat(result.getCreated()).isEqualTo(ORDER_CREATED),
                    () -> assertThat(result.getBuyer()).isEqualTo(buyer),
                    () -> assertThat(result.getSeller()).isEqualTo(seller),
                    () -> assertThat(result.getItem()).isEqualTo(itemDto)
            );
        }

        @Test
        void validInputs_validDto() {

            OrderDto result = instance.entityToDto(null, null, null, null);

            assertThat(result).isNull();
        }

        private OrderEntity prepareDummyOrderEntity() {

            OrderEntity order = new OrderEntity();
            order.setId(ORDER_ID);
            order.setCreated(ORDER_CREATED);

            return order;
        }

        private ItemDto prepareDummyItemDto() {

            ItemDto item = new ItemDto();
            item.setId(DUMMY_ITEM_ID);
            item.setState(DUMMY_ITEM_STATE);
            item.setName(DUMMY_ITEM_NAME);
            item.setValidFrom(DUMMY_ITEM_VALID_FROM);
            item.setValidTo(DUMMY_ITEM_VALID_TO);
            item.setStartingPrice(DUMMY_ITEM_STARTING_PRICE);
            item.setDescription(DUMMY_ITEM_DESCRIPTION);
            item.setPictureUUID(DUMMY_FILE_IDENTIFIER);
            item.setDelivery(prepareDummyDeliveryDto());
            item.setItemHighestBid(prepareDummyHighestBidDto());
            item.setPayment(prepareDummyPaymentDto());
            item.setSeller(prepareDummyUser(
                    DUMMY_SELLER_ID,
                    DUMMY_SELLER_FIRST_NAME,
                    DUMMY_SELLER_LAST_NAME,
                    DUMMY_SELLER_EMAIL,
                    DUMMY_SELLER_DESCRIPTION));

            return item;
        }

        private ItemHighestBidDto prepareDummyHighestBidDto() {

            return new ItemHighestBidDto(
                    DUMMY_ITEM_ID,
                    DUMMY_HIGHEST_BID_AMOUNT,
                    DUMMY_BUYER_ID,
                    DUMMY_HIGHEST_BID_TIME);
        }

        private PaymentDto prepareDummyPaymentDto() {

            PaymentDto payment = new PaymentDto();
            payment.setId(DUMMY_PAYMENT_ID);
            payment.setName(DUMMY_PAYMENT_NAME);
            payment.setDescription(DUMMY_PAYMENT_DESCRIPTION);
            payment.setPrice(DUMMY_PAYMENT_PRICE);

            return payment;
        }

        private DeliveryDto prepareDummyDeliveryDto() {

            DeliveryDto delivery = new DeliveryDto();
            delivery.setId(DUMMY_DELIVERY_ID);
            delivery.setName(DUMMY_DELIVERY_NAME);
            delivery.setDescription(DUMMY_DELIVERY_DESCRIPTION);
            delivery.setPrice(DUMMY_DELIVERY_PRICE);

            return delivery;
        }

        private UserDto prepareDummyUser(long id, String firstName, String lastName, String email, String description) {
            UserDto user = new UserDto();
            user.setId(id);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEmail(email);
            user.setDescription(description);

            AddressDto address = prepareDummyAddressDto();
            user.setAddresses(List.of(address));

            BankAccountDto bankAccount = prepareDummyBankAccountDto();
            user.setBankAccounts(List.of(bankAccount));

            return user;
        }

        private BankAccountDto prepareDummyBankAccountDto() {

            BankAccountDto bankAccount = new BankAccountDto();
            bankAccount.setId(DUMMY_BANK_ACCOUNT_ID);
            bankAccount.setPrefix(DUMMY_BANK_ACCOUNT_PREFIX);
            bankAccount.setNumber(DUMMY_BANK_ACCOUNT_NUMBER);
            bankAccount.setBankCode(DUMMY_BANK_ACCOUNT_CODE);

            return bankAccount;
        }

        private AddressDto prepareDummyAddressDto() {

            AddressDto address = new AddressDto();
            address.setId(DUMMY_ADDRESS_ID);
            address.setCity(DUMMY_CITY);
            address.setStreet(DUMMY_STREET);
            address.setStreetNumber(DUMMY_STREET_NUMBER);
            address.setZipcode(DUMMY_ZIPCODE);

            return address;
        }
    }
}
