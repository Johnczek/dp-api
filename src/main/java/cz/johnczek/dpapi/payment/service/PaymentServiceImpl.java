package cz.johnczek.dpapi.payment.service;

import cz.johnczek.dpapi.payment.dto.PaymentDto;
import cz.johnczek.dpapi.payment.entity.PaymentEntity;
import cz.johnczek.dpapi.payment.mapper.PaymentMapper;
import cz.johnczek.dpapi.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    private final PaymentMapper paymentMapper;

    @Override
    public Collection<PaymentDto> getAllPaymentTypes() {
        return paymentRepository.findAll().stream().map(paymentMapper::entityToDto).collect(Collectors.toList());
    }

    @Override
    public Optional<PaymentEntity> findById(long paymentId) {
        return paymentRepository.findById(paymentId);
    }

    @Override
    public Optional<PaymentDto> findDtoById(long paymentId) {
        return paymentRepository.findById(paymentId).map(paymentMapper::entityToDto);
    }
}
