package cz.johnczek.dpapi.payment.repository;

import cz.johnczek.dpapi.payment.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {

    List<PaymentEntity> findAll();

    Optional<PaymentEntity> findById(long id);

    @Query("select p from ItemEntity i left join i.payment p where i.id in (:itemIds)")
    Set<PaymentEntity> findByItemIds(@Param("itemIds") Set<Long> itemIds);

}
