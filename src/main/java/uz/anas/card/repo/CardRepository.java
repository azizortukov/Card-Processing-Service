package uz.anas.card.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.anas.card.entity.Card;

import java.util.UUID;

public interface CardRepository extends JpaRepository<Card, UUID> {

    @Query(nativeQuery = true, value = """
            SELECT count(card) FROM card
            WHERE status = 'ACTIVE' AND user_id = :userId""")
    Integer findActiveCardsByUserId(Long userId);
}