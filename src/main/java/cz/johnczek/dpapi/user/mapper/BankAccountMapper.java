package cz.johnczek.dpapi.user.mapper;

import cz.johnczek.dpapi.user.dto.BankAccountDto;
import cz.johnczek.dpapi.user.entity.BankAccountEntity;
import cz.johnczek.dpapi.user.request.BankAccountCreationRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BankAccountMapper {

    BankAccountEntity requestToEntity(BankAccountCreationRequest request);

    BankAccountDto entityToDto(BankAccountEntity source);
}
