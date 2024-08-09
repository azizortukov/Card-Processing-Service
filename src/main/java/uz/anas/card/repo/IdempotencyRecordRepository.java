package uz.anas.card.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.anas.card.entity.IdempotencyRecord;

import java.util.UUID;

public interface IdempotencyRecordRepository extends JpaRepository<IdempotencyRecord, UUID> {
}