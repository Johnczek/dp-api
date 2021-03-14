package cz.johnczek.dpapi.item.mapper;

import cz.johnczek.dpapi.delivery.dto.DeliveryDto;
import cz.johnczek.dpapi.delivery.entity.DeliveryEntity;
import cz.johnczek.dpapi.file.entity.FileEntity;
import cz.johnczek.dpapi.file.enums.FileType;
import cz.johnczek.dpapi.item.dto.ItemDto;
import cz.johnczek.dpapi.item.dto.ItemHighestBidDto;
import cz.johnczek.dpapi.item.entity.ItemEntity;
import cz.johnczek.dpapi.item.enums.ItemState;
import cz.johnczek.dpapi.item.request.ItemCreationRequest;
import cz.johnczek.dpapi.payment.dto.PaymentDto;
import cz.johnczek.dpapi.payment.entity.PaymentEntity;
import cz.johnczek.dpapi.user.dto.AddressDto;
import cz.johnczek.dpapi.user.dto.BankAccountDto;
import cz.johnczek.dpapi.user.dto.UserDto;
import cz.johnczek.dpapi.user.entity.AddressEntity;
import cz.johnczek.dpapi.user.entity.BankAccountEntity;
import cz.johnczek.dpapi.user.entity.UserEntity;
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
class ItemMapperUnitTest {

    private static final long DUMMY_ITEM_ID = 1L;
    private static final ItemState DUMMY_ITEM_STATE = ItemState.ACTIVE;
    private static final String DUMMY_ITEM_NAME = "Dummy name";
    private static final BigDecimal DUMMY_ITEM_STARTING_PRICE = BigDecimal.ONE;
    private static final LocalDateTime DUMMY_ITEM_VALID_FROM = LocalDateTime.now();
    private static final LocalDateTime DUMMY_ITEM_VALID_TO = LocalDateTime.now().plusDays(10);
    private static final long DUMMY_FILE_ID = 100L;
    private static final String DUMMY_ITEM_DESCRIPTION = "Lorem ipsum....";
    private static final String DUMMY_FILE_IDENTIFIER = "8a658007-7c3a-4a01-9278-9389f93af3c7";
    private static final long DUMMY_DELIVERY_ID = 2L;
    private static final String DUMMY_DELIVERY_NAME = "Delivery name";
    private static final String DUMMY_DELIVERY_DESCRIPTION = "Delivery description";
    private static final BigDecimal DUMMY_DELIVERY_PRICE = BigDecimal.ONE;
    private static final long DUMMY_PAYMENT_ID = 3L;
    private static final String DUMMY_PAYMENT_NAME = "Payment name";
    private static final String DUMMY_PAYMENT_DESCRIPTION = "Payment description";
    private static final BigDecimal DUMMY_PAYMENT_PRICE = BigDecimal.TEN;
    private static final long DUMMY_USER_ID = 4L;
    private static final String DUMMY_USER_FIRST_NAME = "Firstname";
    private static final String DUMMY_USER_LAST_NAME = "Lastname";
    private static final String DUMMY_USER_DESCRIPTION = "User description";
    private static final String DUMMY_USER_EMAIL = "email@email.com";
    private static final BigDecimal DUMMY_HIGHEST_BID_AMOUNT = new BigDecimal("1000");
    private static final long DUMMY_HIGHEST_BID_USER_ID = 55L;
    private static final LocalDateTime DUMMY_HIGHEST_BID_TIME = LocalDateTime.now().minusMinutes(10);
    private static final long DUMMY_ADDRESS_ID = 1000L;
    private static final String DUMMY_CITY = "City";
    private static final String DUMMY_STREET = "Street";
    private static final String DUMMY_STREET_NUMBER = "StreetNr";
    private static final String DUMMY_ZIPCODE = "12300";
    private static final long DUMMY_BANK_ACCOUNT_ID = 2000L;
    private static final long DUMMY_BANK_ACCOUNT_PREFIX = 100L;
    private static final long DUMMY_BANK_ACCOUNT_NUMBER = 155050;
    private static final int DUMMY_BANK_ACCOUNT_CODE = 1010;

    @InjectMocks
    private ItemMapperImpl instance;

    @Nested
    class EntityToDto {

        @Test
        void nullItem_nullDto() {

            ItemDto result = instance.entityToDto(null);

            assertThat(result).isNull();
        }

        @Test
        void nullItemAndParameters_nullDto() {

            ItemDto result = instance.entityToDto(null, null, null, null, null);

            assertThat(result).isNull();
        }

        @Test
        void validItem_mappedDto() {

            ItemDto result = instance.entityToDto(prepareDummyItemEntity());

            performBasicAsserts(result);
            assertThat(result.getItemHighestBid()).isNull();
        }

        @Test
        void validItemAndParameters_mappedDto() {

            ItemDto result = instance.entityToDto(
                    prepareDummyItemEntity(),
                    prepareDummyDeliveryDto(),
                    prepareDummyPaymentDto(),
                    prepareDummySellerDto(),
                    prepareDummyHighestBidDto());

            performBasicAsserts(result);

            assertAll(
                    () -> assertThat(result.getItemHighestBid()).isNotNull(),
                    () -> assertThat(result.getItemHighestBid().getItemId()).isEqualTo(DUMMY_ITEM_ID),
                    () -> assertThat(result.getItemHighestBid().getAmount()).isEqualTo(DUMMY_HIGHEST_BID_AMOUNT),
                    () -> assertThat(result.getItemHighestBid().getUserId()).isEqualTo(DUMMY_HIGHEST_BID_USER_ID),
                    () -> assertThat(result.getItemHighestBid().getTime()).isEqualTo(DUMMY_HIGHEST_BID_TIME)
            );
        }

        private void performBasicAsserts(ItemDto result) {
            assertAll(
                    () -> assertThat(result).isNotNull(),
                    () -> assertThat(result.getId()).isEqualTo(DUMMY_ITEM_ID),
                    () -> assertThat(result.getName()).isEqualTo(DUMMY_ITEM_NAME),
                    () -> assertThat(result.getDescription()).isEqualTo(DUMMY_ITEM_DESCRIPTION),
                    () -> assertThat(result.getState()).isEqualTo(DUMMY_ITEM_STATE),
                    () -> assertThat(result.getValidFrom()).isEqualTo(DUMMY_ITEM_VALID_FROM),
                    () -> assertThat(result.getValidTo()).isEqualTo(DUMMY_ITEM_VALID_TO),
                    () -> assertThat(result.getStartingPrice()).isEqualTo(DUMMY_ITEM_STARTING_PRICE),
                    () -> assertThat(result.getPictureUUID()).isEqualTo(DUMMY_FILE_IDENTIFIER),
                    () -> assertAll(
                            () -> assertThat(result.getPayment()).isNotNull(),
                            () -> assertThat(result.getPayment().getId()).isEqualTo(DUMMY_PAYMENT_ID),
                            () -> assertThat(result.getPayment().getName()).isEqualTo(DUMMY_PAYMENT_NAME),
                            () -> assertThat(result.getPayment().getPrice()).isEqualTo(DUMMY_PAYMENT_PRICE),
                            () -> assertThat(result.getPayment().getDescription()).isEqualTo(DUMMY_PAYMENT_DESCRIPTION)
                    ),
                    () -> assertAll(
                            () -> assertThat(result.getDelivery()).isNotNull(),
                            () -> assertThat(result.getDelivery().getId()).isEqualTo(DUMMY_DELIVERY_ID),
                            () -> assertThat(result.getDelivery().getName()).isEqualTo(DUMMY_DELIVERY_NAME),
                            () -> assertThat(result.getDelivery().getPrice()).isEqualTo(DUMMY_DELIVERY_PRICE),
                            () -> assertThat(result.getDelivery().getDescription()).isEqualTo(DUMMY_DELIVERY_DESCRIPTION)
                    ),
                    () -> assertAll(
                            () -> assertThat(result.getSeller()).isNotNull(),
                            () -> assertThat(result.getSeller().getId()).isEqualTo(DUMMY_USER_ID),
                            () -> assertThat(result.getSeller().getFirstName()).isEqualTo(DUMMY_USER_FIRST_NAME),
                            () -> assertThat(result.getSeller().getLastName()).isEqualTo(DUMMY_USER_LAST_NAME),
                            () -> assertThat(result.getSeller().getDescription()).isEqualTo(DUMMY_USER_DESCRIPTION),
                            () -> assertThat(result.getSeller().getEmail()).isEqualTo(DUMMY_USER_EMAIL),
                            () -> assertAll(
                                    () -> assertThat(result.getSeller().getAddresses()).isNotEmpty(),
                                    () -> assertThat(result.getSeller().getAddresses().get(0).getId()).isEqualTo(DUMMY_ADDRESS_ID),
                                    () -> assertThat(result.getSeller().getAddresses().get(0).getCity()).isEqualTo(DUMMY_CITY),
                                    () -> assertThat(result.getSeller().getAddresses().get(0).getStreet()).isEqualTo(DUMMY_STREET),
                                    () -> assertThat(result.getSeller().getAddresses().get(0).getStreetNumber()).isEqualTo(DUMMY_STREET_NUMBER)
                            ),
                            () -> assertAll(
                                    () -> assertThat(result.getSeller().getBankAccounts()).isNotEmpty(),
                                    () -> assertThat(result.getSeller().getBankAccounts().get(0).getId()).isEqualTo(DUMMY_BANK_ACCOUNT_ID),
                                    () -> assertThat(result.getSeller().getBankAccounts().get(0).getPrefix()).isEqualTo(DUMMY_BANK_ACCOUNT_PREFIX),
                                    () -> assertThat(result.getSeller().getBankAccounts().get(0).getNumber()).isEqualTo(DUMMY_BANK_ACCOUNT_NUMBER),
                                    () -> assertThat(result.getSeller().getBankAccounts().get(0).getBankCode()).isEqualTo(DUMMY_BANK_ACCOUNT_CODE)
                            )
                    )
            );
        }
    }


    @Nested
    class CreationRequestToEntity {

        @Test
        void nullInput_nullOutput() {

            ItemEntity item = instance.creationRequestToEntity(null);

            assertThat(item).isNull();

        }

        @Test
        void validInput_validEntityCreated() {

            ItemEntity item = instance.creationRequestToEntity(prepareDummyItemCreationRequest());

            assertAll(
                    () -> assertThat(item).isNotNull(),
                    () -> assertThat(item.getName()).isEqualTo(DUMMY_ITEM_NAME),
                    () -> assertThat(item.getDescription()).isEqualTo(DUMMY_ITEM_DESCRIPTION),
                    () -> assertThat(item.getValidFrom()).isEqualTo(DUMMY_ITEM_VALID_FROM),
                    () -> assertThat(item.getValidTo()).isEqualTo(DUMMY_ITEM_VALID_TO),
                    () -> assertThat(item.getStartingPrice()).isEqualTo(DUMMY_ITEM_STARTING_PRICE)
            );
        }
    }

    private ItemCreationRequest prepareDummyItemCreationRequest() {
        ItemCreationRequest request = new ItemCreationRequest();
        request.setName(DUMMY_ITEM_NAME);
        request.setDescription(DUMMY_ITEM_DESCRIPTION);
        request.setValidFrom(DUMMY_ITEM_VALID_FROM);
        request.setValidTo(DUMMY_ITEM_VALID_TO);
        request.setStartingPrice(DUMMY_ITEM_STARTING_PRICE.longValue());

        return request;
    }

    private ItemHighestBidDto prepareDummyHighestBidDto() {

        return new ItemHighestBidDto(
                DUMMY_ITEM_ID,
                DUMMY_HIGHEST_BID_AMOUNT,
                DUMMY_HIGHEST_BID_USER_ID,
                DUMMY_HIGHEST_BID_TIME);
    }

    private ItemEntity prepareDummyItemEntity() {

        ItemEntity item = new ItemEntity();
        item.setId(DUMMY_ITEM_ID);
        item.setState(DUMMY_ITEM_STATE);
        item.setName(DUMMY_ITEM_NAME);
        item.setValidFrom(DUMMY_ITEM_VALID_FROM);
        item.setValidTo(DUMMY_ITEM_VALID_TO);
        item.setStartingPrice(DUMMY_ITEM_STARTING_PRICE);
        item.setDescription(DUMMY_ITEM_DESCRIPTION);
        item.setPicture(prepareDummyFileEntity());
        item.setDelivery(prepareDummyDeliveryEntity());
        item.setPayment(prepareDummyPaymentEntity());
        item.setSeller(prepareDummySellerEntity());

        return item;
    }

    private UserEntity prepareDummySellerEntity() {
        UserEntity user = new UserEntity();
        user.setId(DUMMY_USER_ID);
        user.setFirstName(DUMMY_USER_FIRST_NAME);
        user.setLastName(DUMMY_USER_LAST_NAME);
        user.setEmail(DUMMY_USER_EMAIL);
        user.setDescription(DUMMY_USER_DESCRIPTION);

        AddressEntity address = prepareDummyAddressEntity();
        user.setAddresses(List.of(address));

        BankAccountEntity bankAccount = prepareDummyBankAccountEntity();
        user.setBankAccounts(List.of(bankAccount));

        return user;
    }

    private BankAccountEntity prepareDummyBankAccountEntity() {

        BankAccountEntity bankAccount = new BankAccountEntity();
        bankAccount.setId(DUMMY_BANK_ACCOUNT_ID);
        bankAccount.setPrefix(DUMMY_BANK_ACCOUNT_PREFIX);
        bankAccount.setNumber(DUMMY_BANK_ACCOUNT_NUMBER);
        bankAccount.setBankCode(DUMMY_BANK_ACCOUNT_CODE);

        return bankAccount;
    }

    private BankAccountDto prepareDummyBankAccountDto() {

        BankAccountDto bankAccount = new BankAccountDto();
        bankAccount.setId(DUMMY_BANK_ACCOUNT_ID);
        bankAccount.setPrefix(DUMMY_BANK_ACCOUNT_PREFIX);
        bankAccount.setNumber(DUMMY_BANK_ACCOUNT_NUMBER);
        bankAccount.setBankCode(DUMMY_BANK_ACCOUNT_CODE);

        return bankAccount;
    }

    private AddressEntity prepareDummyAddressEntity() {

        AddressEntity address = new AddressEntity();
        address.setId(DUMMY_ADDRESS_ID);
        address.setCity(DUMMY_CITY);
        address.setStreet(DUMMY_STREET);
        address.setStreetNumber(DUMMY_STREET_NUMBER);
        address.setZipcode(DUMMY_ZIPCODE);

        return address;
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

    private UserDto prepareDummySellerDto() {
        UserDto user = new UserDto();
        user.setId(DUMMY_USER_ID);
        user.setFirstName(DUMMY_USER_FIRST_NAME);
        user.setLastName(DUMMY_USER_LAST_NAME);
        user.setEmail(DUMMY_USER_EMAIL);
        user.setDescription(DUMMY_USER_DESCRIPTION);

        AddressDto address = prepareDummyAddressDto();
        user.setAddresses(List.of(address));

        BankAccountDto bankAccount = prepareDummyBankAccountDto();
        user.setBankAccounts(List.of(bankAccount));

        return user;
    }

    private PaymentEntity prepareDummyPaymentEntity() {

        PaymentEntity payment = new PaymentEntity();
        payment.setId(DUMMY_PAYMENT_ID);
        payment.setName(DUMMY_PAYMENT_NAME);
        payment.setDescription(DUMMY_PAYMENT_DESCRIPTION);
        payment.setPrice(DUMMY_PAYMENT_PRICE);

        return payment;
    }

    private PaymentDto prepareDummyPaymentDto() {

        PaymentDto payment = new PaymentDto();
        payment.setId(DUMMY_PAYMENT_ID);
        payment.setName(DUMMY_PAYMENT_NAME);
        payment.setDescription(DUMMY_PAYMENT_DESCRIPTION);
        payment.setPrice(DUMMY_PAYMENT_PRICE);

        return payment;
    }

    private DeliveryEntity prepareDummyDeliveryEntity() {

        DeliveryEntity delivery = new DeliveryEntity();
        delivery.setId(DUMMY_DELIVERY_ID);
        delivery.setName(DUMMY_DELIVERY_NAME);
        delivery.setDescription(DUMMY_DELIVERY_DESCRIPTION);
        delivery.setPrice(DUMMY_DELIVERY_PRICE);

        return delivery;
    }

    private DeliveryDto prepareDummyDeliveryDto() {

        DeliveryDto delivery = new DeliveryDto();
        delivery.setId(DUMMY_DELIVERY_ID);
        delivery.setName(DUMMY_DELIVERY_NAME);
        delivery.setDescription(DUMMY_DELIVERY_DESCRIPTION);
        delivery.setPrice(DUMMY_DELIVERY_PRICE);

        return delivery;
    }

    private FileEntity prepareDummyFileEntity() {

        FileEntity file = new FileEntity();
        file.setId(DUMMY_FILE_ID);
        file.setFileExtension(".png");
        file.setFileIdentifier(DUMMY_FILE_IDENTIFIER);
        file.setType(FileType.ITEM_PICTURE);

        return file;
    }
}
