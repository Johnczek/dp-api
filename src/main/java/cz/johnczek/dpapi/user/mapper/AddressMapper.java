package cz.johnczek.dpapi.user.mapper;

import cz.johnczek.dpapi.user.dto.AddressDto;
import cz.johnczek.dpapi.user.entity.AddressEntity;
import cz.johnczek.dpapi.user.request.AddressCreationRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    AddressEntity requestToEntity(AddressCreationRequest request);

    @Mapping(source = "source.user.id", target = "userId")
    AddressDto entityToDto(AddressEntity source);
}
