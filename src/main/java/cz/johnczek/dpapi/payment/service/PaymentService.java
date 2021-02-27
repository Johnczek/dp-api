package cz.johnczek.dpapi.payment.service;

import cz.johnczek.dpapi.payment.dto.PaymentDto;
import cz.johnczek.dpapi.payment.entity.PaymentEntity;
import lombok.NonNull;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface PaymentService {

    /**
     * @return all possible payment methods
     */
    Collection<PaymentDto> getAllPaymentTypes();

    /**
     * @param paymentId id of payment we want to find
     * @return entity of desired payment, empty optional in case we are unable to retrieve this payment
     */
    Optional<PaymentEntity> findById(long paymentId);

    /**
     *
     * @param itemIds ids of items for which we want to find payment methods
     * @return payment methods used for given items, where key is id of payment method and value is dto holding more information
     */
    Map<Long, PaymentDto> findByItemIds(@NonNull Set<Long> itemIds);
}
