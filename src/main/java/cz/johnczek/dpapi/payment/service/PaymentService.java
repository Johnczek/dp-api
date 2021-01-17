package cz.johnczek.dpapi.payment.service;

import cz.johnczek.dpapi.payment.dto.PaymentDto;
import cz.johnczek.dpapi.payment.entity.PaymentEntity;

import java.util.Collection;
import java.util.Optional;

public interface PaymentService {

    Collection<PaymentDto> getAllPaymentTypes();

    Optional<PaymentEntity> findById(long paymentId);

    Optional<PaymentDto> findDtoById(long paymentId);
}
