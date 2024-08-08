package uz.anas.card.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.anas.card.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
}