package uz.anas.card.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.anas.card.entity.Transaction;

import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
}