package cz.johnczek.dpapi.item.repository;

import cz.johnczek.dpapi.item.entity.ItemEntity;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    @Query("select i from ItemEntity i " +
            "left join fetch i.picture " +
            "left join i.delivery " +
            "left join i.payment " +
            "left join i.seller")
    Set<ItemEntity> findAllWithFieldsFetched();

    @Query("select i from ItemEntity i " +
            "left join fetch i.picture " +
            "left join fetch i.delivery " +
            "left join fetch i.payment " +
            "left join fetch i.seller " +
            "where i.id = :itemId")
    Optional<ItemEntity> findByIdWithFieldsFetched(@Param("itemId") long itemId);
}
