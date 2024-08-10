package uz.anas.card.component;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import uz.anas.card.entity.Role;
import uz.anas.card.entity.User;
import uz.anas.card.entity.enums.RoleName;
import uz.anas.card.repo.RoleRepository;
import uz.anas.card.repo.UserRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class Runner implements CommandLineRunner {


    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        // Saving all roles if not saved yet!
        if (roleRepository.findAll().isEmpty()) {
            for (RoleName role : RoleName.values()) {
                roleRepository.save(Role.builder()
                        .name(role)
                        .build());
            }
        }

        // Saving two mock users if users haven't been saved yet!
        if (userRepository.findAll().isEmpty()) {
            userRepository.save(User.builder()
                    .roles(List.of(roleRepository.findByName(RoleName.ROLE_ADMIN)))
                    .email("admin@gmail.com")
                    .password(
                            passwordEncoder.encode("12345678")
                    )
                    .fullName("John")
                    .build());
            userRepository.save(User.builder()
                    .roles(List.of(roleRepository.findByName(RoleName.ROLE_CLIENT)))
                    .email("client@gmail.com")
                    .password(
                            passwordEncoder.encode("12345678")
                    )
                    .fullName("Sarah")
                    .build());
        }
    }
}
