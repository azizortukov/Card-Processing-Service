package uz.anas.card.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.anas.card.entity.Card;

import java.util.List;
import java.util.UUID;

public interface CardRepository extends JpaRepository<Card, UUID> {

    @Query(nativeQuery = true, value = """
        select * from card where status = 'ACTIVE'""")
    List<Card> findActiveCardsByUserId(Long user_id);
}