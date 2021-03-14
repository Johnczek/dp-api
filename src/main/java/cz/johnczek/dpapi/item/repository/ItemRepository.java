package cz.johnczek.dpapi.item.repository;

import cz.johnczek.dpapi.item.entity.ItemEntity;
import cz.johnczek.dpapi.item.enums.ItemState;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ItemRepository extends JpaRepository<ItemEntity, Long> {

    @Query("select i from ItemEntity i " +
            "left join fetch i.picture " +
            "left join i.delivery " +
            "left join i.payment " +
            "left join i.seller " +
            "where i.id in (:itemIds)")
    Set<ItemEntity> findByItemIdsWithFields(@NonNull @Param("itemIds") Set<Long> itemIds);

    @Query("select i.id from ItemEntity i " +
            "where i.state = cz.johnczek.dpapi.item.enums.ItemState.ACTIVE")
    Set<Long> findAllActiveIds();

    @Query("select i.id from ItemEntity i " +
            "left join i.seller s " +
            "where s.id = :sellerId")
    Set<Long> findAllActiveIdsBySellerId(@Param("sellerId") long sellerId);

    @Query("select i from ItemEntity i " +
            "left join fetch i.picture " +
            "left join fetch i.delivery " +
            "left join fetch i.payment " +
            "left join fetch i.seller " +
            "left join fetch i.bids " +
            "where i.id = :itemId")
    Optional<ItemEntity> findByIdWithFieldsFetched(@Param("itemId") long itemId);

    @Query(value = "select i.id " +
            "from item i " +
            "         join item_bid ib on (i.id = ib.item_id) " +
            "where (item_id, amount) in " +
            "      (select item_id, max(amount) " +
            "       from item i " +
            "                join item_bid ib on (i.id = ib.item_id) " +
            "       group by ib.item_id) " +
            "  and ib.buyer_id = :buyerId and i.state = 'AUCTIONED'", nativeQuery = true)
    Set<Long> findAllIdsForCartByBuyerId(@Param("buyerId") long buyerId);

    @Query("select i.state from ItemEntity i where i.id = :itemId")
    Optional<ItemState> findStateByItemId(@Param("itemId") long itemId);

    @Query("select distinct i " +
            "from ItemEntity i " +
            "left join fetch i.bids " +
            "where i.state = cz.johnczek.dpapi.item.enums.ItemState.ACTIVE " +
            "   and i.validTo <= :currentTime")
    List<ItemEntity> findActiveWithExpiredValidToAndBidsFetched(@NonNull @Param("currentTime") LocalDateTime currentTime);
}
