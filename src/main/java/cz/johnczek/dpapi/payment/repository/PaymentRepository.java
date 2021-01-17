package cz.johnczek.dpapi.payment.repository;

import cz.johnczek.dpapi.payment.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {

    List<PaymentEntity> findAll();

    Optional<PaymentEntity> findById(long id);

}
