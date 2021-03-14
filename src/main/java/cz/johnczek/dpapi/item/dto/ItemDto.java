package cz.johnczek.dpapi.item.dto;

import cz.johnczek.dpapi.delivery.dto.DeliveryDto;
import cz.johnczek.dpapi.item.enums.ItemState;
import cz.johnczek.dpapi.payment.dto.PaymentDto;
import cz.johnczek.dpapi.user.dto.UserDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
public class ItemDto {

    private Long id;

    private String name;

    private String description;

    private ItemState state;

    private LocalDateTime validFrom;

    private LocalDateTime validTo;

    private BigDecimal startingPrice;

    private ItemHighestBidDto itemHighestBid;

    private PaymentDto payment;

    private DeliveryDto delivery;

    private UserDto seller;

    private String pictureUUID;
}
