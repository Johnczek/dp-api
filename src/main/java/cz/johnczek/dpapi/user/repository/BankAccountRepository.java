package cz.johnczek.dpapi.user.repository;

import cz.johnczek.dpapi.user.entity.BankAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BankAccountRepository extends JpaRepository<BankAccountEntity, Long> {

    @Query("select ba from BankAccountEntity ba where ba.id = :bankAccountId and ba.user.id = :userId")
    Optional<BankAccountEntity> findByIdAndUserId(@Param("bankAccountId") long bankAccountId, @Param("userId") long userId);

    @Query("select ba from BankAccountEntity ba where ba.user.id = :userId")
    List<BankAccountEntity> findByUserId(@Param("userId") long userId);

    @Query("select ba from BankAccountEntity ba where ba.user.id in (:userIds)")
    List<BankAccountEntity> findByUserIds(@Param("userIds") List<Long> userIds);
}
