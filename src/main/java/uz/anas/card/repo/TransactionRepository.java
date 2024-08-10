package uz.anas.card.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.anas.card.entity.Transaction;
import uz.anas.card.model.projection.TransactionProjection;

import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    @Query(nativeQuery = true, value = """
            SELECT * FROM transaction t
            WHERE (:cardId IS NULL OR t.card_id = :cardId) AND
            (:type IS NULL OR t.transaction_type = :type) AND
            (:transactionId IS NULL OR t.id = :transactionId) AND
            (:externalId IS NULL OR t.external_id = :externalId) AND
            (:currency IS NULL OR t.currency = :currency)""")
    Page<TransactionProjection> findAll(UUID cardId, String type, UUID transactionId, String externalId, String currency, Pageable pageable);

}