package uz.anas.card.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.anas.card.entity.Role;
import uz.anas.card.entity.enums.RoleName;

public interface RoleRepository extends JpaRepository<Role, Integer> {

    Role findByName(RoleName name);

}