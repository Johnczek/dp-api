package cz.johnczek.dpapi.delivery.mapper;

import cz.johnczek.dpapi.delivery.dto.DeliveryDto;
import cz.johnczek.dpapi.delivery.entity.DeliveryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DeliveryMapper {

    @Mapping(target = "logoUUID", source = "logo.fileIdentifier")
    DeliveryDto entityToDto(DeliveryEntity source);
}
