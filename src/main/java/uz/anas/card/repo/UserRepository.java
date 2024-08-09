package uz.anas.card.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.anas.card.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}