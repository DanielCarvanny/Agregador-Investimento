package tech.buildrun.agregadorinvestimentos.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tech.buildrun.agregadorinvestimentos.entity.Account;

import java.util.List;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {
    @Modifying
    @Transactional
    @Query("DELETE FROM Account a WHERE a.user.userId = :userId")
    void deleteAllByUserId(@Param("userId") UUID userId);

    List<Account> findByUserUserId(UUID id);
}
