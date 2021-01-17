package cz.johnczek.dpapi.payment.mapper;

import cz.johnczek.dpapi.payment.dto.PaymentDto;
import cz.johnczek.dpapi.payment.entity.PaymentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    @Mapping(target = "logoUUID", source = "logo.fileIdentifier")
    PaymentDto entityToDto(PaymentEntity source);
}
