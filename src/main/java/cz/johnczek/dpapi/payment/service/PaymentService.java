package cz.johnczek.dpapi.payment.service;

import cz.johnczek.dpapi.payment.dto.PaymentDto;
import cz.johnczek.dpapi.payment.entity.PaymentEntity;
import lombok.NonNull;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface PaymentService {

    Collection<PaymentDto> getAllPaymentTypes();

    Optional<PaymentEntity> findById(long paymentId);

    Optional<PaymentDto> findDtoById(long paymentId);

    Map<Long, PaymentDto> findByItemIds(@NonNull Set<Long> itemIds);
}
