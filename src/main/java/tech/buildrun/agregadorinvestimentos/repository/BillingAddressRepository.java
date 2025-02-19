package tech.buildrun.agregadorinvestimentos.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tech.buildrun.agregadorinvestimentos.entity.BillingAddress;

import java.util.UUID;

@Repository
public interface BillingAddressRepository extends JpaRepository<BillingAddress, UUID> {

    @Modifying
    @Transactional
    @Query("DELETE FROM BillingAddress b WHERE b.account.accountId = :accountId")
    void deleteAllByAccountId(@Param("accountId") UUID accountId);
}

