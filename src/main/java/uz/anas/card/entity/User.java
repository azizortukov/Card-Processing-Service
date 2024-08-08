package uz.anas.card.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;
    private String fullName;
    @NotNull(message = "Role cannot be null!")
    @ManyToMany
    private List<Role> roles;
    @Column(unique = true)
    @Email(message = "Please, provide email!")
    private String email;
    @Size(message = "Password length must be at least 8!", min = 8)
    @NotBlank(message = "Password cannot be blank!")
    private String password;


}