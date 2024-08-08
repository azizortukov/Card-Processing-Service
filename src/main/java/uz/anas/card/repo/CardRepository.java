package uz.anas.card.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.anas.card.entity.Card;

import java.util.UUID;

public interface CardRepository extends JpaRepository<Card, UUID> {
}