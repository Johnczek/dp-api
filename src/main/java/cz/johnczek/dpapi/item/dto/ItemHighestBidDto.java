package cz.johnczek.dpapi.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ItemHighestBidDto {

    private final Long itemId;

    private final BigDecimal amount;

    private final Long userId;

    private final LocalDateTime time;
}
