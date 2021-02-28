package cz.johnczek.dpapi.item.response;

import cz.johnczek.dpapi.item.dto.ItemHighestBidDto;
import cz.johnczek.dpapi.item.enums.ItemState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ItemWsInfoResponse {

    private ItemState itemState;

    private ItemHighestBidDto itemHighestBidDto;
}
