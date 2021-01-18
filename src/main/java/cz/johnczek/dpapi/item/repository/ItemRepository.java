package cz.johnczek.dpapi.item.repository;

import cz.johnczek.dpapi.item.entity.ItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface ItemRepository extends JpaRepository<ItemEntity, Long> {

    @Query("select i from ItemEntity i " +
            "left join i.picture " +
            "left join i.delivery " +
            "left join i.payment " +
            "left join i.seller " +
            "where i.id in (:itemIds)")
    Set<ItemEntity> findByItemIdsWithFields(@Param("itemIds") Set<Long> itemIds);

    @Query("select i from ItemEntity i " +
            "left join i.picture " +
            "left join i.delivery " +
            "left join i.payment " +
            "left join i.seller")
    Set<ItemEntity> findAllWithFieldsFetched();
}
