package uz.anas.card.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.anas.card.entity.User;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
}