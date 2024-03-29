package cz.johnczek.dpapi.item.repository;

import cz.johnczek.dpapi.item.dto.ItemHighestBidDto;
import cz.johnczek.dpapi.item.entity.ItemBidEntity;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface ItemBidRepository extends JpaRepository<ItemBidEntity, Long> {

    @Query("select new cz.johnczek.dpapi.item.dto.ItemHighestBidDto(ib.item.id, ib.amount, ib.buyer.id, ib.time) " +
            "from ItemBidEntity ib " +
            "where ib.item.id in (:itemIds)")
    Collection<ItemHighestBidDto> findBidsByItemIds(@NonNull @Param("itemIds") Set<Long> itemIds);

    @Query("select ib from ItemBidEntity ib where ib.item.id = :itemId order by ib.id desc")
    List<ItemBidEntity> findBidsByItemOrderByCreated(@Param("itemId") long itemId);

    @Modifying
    @Query("delete from ItemBidEntity ib where ib.item.id = :itemId")
    void deleteAllByItemId(@Param("itemId") long itemId);
}
