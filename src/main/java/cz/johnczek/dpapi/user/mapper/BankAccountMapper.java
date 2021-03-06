package cz.johnczek.dpapi.user.mapper;

import cz.johnczek.dpapi.user.dto.BankAccountDto;
import cz.johnczek.dpapi.user.entity.BankAccountEntity;
import cz.johnczek.dpapi.user.request.BankAccountCreationRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BankAccountMapper {

    BankAccountEntity requestToEntity(BankAccountCreationRequest request);

    @Mapping(source = "source.user.id", target = "userId")
    BankAccountDto entityToDto(BankAccountEntity source);
}
