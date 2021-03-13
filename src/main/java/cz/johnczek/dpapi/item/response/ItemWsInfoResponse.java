package cz.johnczek.dpapi.item.response;

import cz.johnczek.dpapi.item.dto.ItemHighestBidDto;
import cz.johnczek.dpapi.item.enums.ItemState;
import cz.johnczek.dpapi.item.enums.WsBidState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ItemWsInfoResponse {

    private ItemState itemState;

    private ItemHighestBidDto itemHighestBidDto;

    private WsBidState state;

    private String message;
}
