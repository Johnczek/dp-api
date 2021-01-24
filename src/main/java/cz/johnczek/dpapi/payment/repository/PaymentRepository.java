package cz.johnczek.dpapi.payment.repository;

import cz.johnczek.dpapi.payment.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {

    @Query("select p " +
            "from PaymentEntity p " +
            "left join fetch p.logo ")
    List<PaymentEntity> findAll();

    @Query("select p " +
            "from PaymentEntity p " +
            "left join fetch p.logo " +
            "where p.id = :id")
    Optional<PaymentEntity> findById(long id);

    @Query("select p " +
            "from ItemEntity i " +
            "left join i.payment p " +
            "left join fetch p.logo " +
            "where i.id in (:itemIds)")
    Set<PaymentEntity> findByItemIds(@Param("itemIds") Set<Long> itemIds);

}
