package cz.johnczek.dpapi.item.mapper;

import cz.johnczek.dpapi.item.dto.ItemHighestBidDto;
import cz.johnczek.dpapi.item.entity.ItemBidEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ItemBidMapper {

    @Mapping(source = "item.id", target = "itemId")
    @Mapping(source = "buyer.id", target = "userId")
    ItemHighestBidDto entityToHighestBidDto(ItemBidEntity source);
}
