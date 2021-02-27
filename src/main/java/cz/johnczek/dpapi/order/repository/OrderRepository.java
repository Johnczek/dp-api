package cz.johnczek.dpapi.order.repository;

import cz.johnczek.dpapi.order.entity.OrderEntity;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    @Query("select o.id from OrderEntity o where o.buyer.id = :buyerId")
    List<Long> findOrderIdsByBuyerId(@Param("buyerId") long buyerId);

    @Query("select o from OrderEntity o where o.id in ( orderIds )")
    List<OrderEntity> findOrdersByIds(@Param("orderIds") @NonNull List<Long> orderIds);
}
