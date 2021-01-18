package cz.johnczek.dpapi.item.mapper;

import cz.johnczek.dpapi.delivery.dto.DeliveryDto;
import cz.johnczek.dpapi.item.dto.ItemDto;
import cz.johnczek.dpapi.item.entity.ItemEntity;
import cz.johnczek.dpapi.payment.dto.PaymentDto;
import cz.johnczek.dpapi.user.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    @Mapping(target = "delivery", ignore = true)
    @Mapping(target = "payment", ignore = true)
    @Mapping(target = "seller", ignore = true)
    @Mapping(target = "pictureUUID", source = "source.picture.fileIdentifier")
    @Mapping(target = "id", source = "source.id")
    @Mapping(target = "name", source = "source.name")
    @Mapping(target = "description", source = "source.description")
    ItemDto entityToDto(ItemEntity source, DeliveryDto delivery, PaymentDto payment, UserDto seller);

}
