package cz.johnczek.dpapi.payment.service;

import com.google.common.collect.Maps;
import cz.johnczek.dpapi.payment.dto.PaymentDto;
import cz.johnczek.dpapi.payment.entity.PaymentEntity;
import cz.johnczek.dpapi.payment.mapper.PaymentMapper;
import cz.johnczek.dpapi.payment.repository.PaymentRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    private final PaymentMapper paymentMapper;

    @Override
    @Transactional(readOnly = true)
    public Collection<PaymentDto> getAllPaymentTypes() {
        return paymentRepository.findAll().stream().map(paymentMapper::entityToDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PaymentEntity> findById(long paymentId) {
        return paymentRepository.findById(paymentId);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<Long, PaymentDto> findByItemIds(@NonNull Set<Long> itemIds) {

        if (CollectionUtils.isEmpty(itemIds)) {
            return Collections.emptyMap();
        }

        List<PaymentDto> paymentsByIds = paymentRepository.findByItemIds(itemIds).stream()
                .map(paymentMapper::entityToDto).collect(Collectors.toList());

        return Maps.uniqueIndex(paymentsByIds, PaymentDto::getId);
    }
}
