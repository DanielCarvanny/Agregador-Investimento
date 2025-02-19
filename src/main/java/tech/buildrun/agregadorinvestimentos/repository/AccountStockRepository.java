package tech.buildrun.agregadorinvestimentos.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tech.buildrun.agregadorinvestimentos.entity.AccountStock;
import tech.buildrun.agregadorinvestimentos.entity.AccountStockId;

import java.util.UUID;


@Repository
public interface AccountStockRepository extends JpaRepository<AccountStock, AccountStockId> {

    @Modifying
    @Transactional
    @Query("DELETE FROM AccountStock a WHERE a.account.accountId = :accountId")
    void deleteAllByAccountId(@Param("accountId") UUID accountId);
}
