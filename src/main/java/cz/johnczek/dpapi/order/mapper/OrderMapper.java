package cz.johnczek.dpapi.order.mapper;

import cz.johnczek.dpapi.item.dto.ItemDto;
import cz.johnczek.dpapi.order.dto.OrderDto;
import cz.johnczek.dpapi.order.entity.OrderEntity;
import cz.johnczek.dpapi.user.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "orderId", source = "source.id")
    @Mapping(target = "item", source = "item")
    @Mapping(target = "buyer", source = "buyer")
    @Mapping(target = "seller", source = "seller")
    @Mapping(target = "created", source = "source.created")
    OrderDto entityToDto(OrderEntity source, UserDto buyer, UserDto seller, ItemDto item);
}
