package uz.anas.card.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import uz.anas.card.entity.User;

import java.io.Serializable;

/**
 * DTO for {@link User}
 */
public record LoginDto(
        @NotBlank(message = "Email cannot be blank!")
        @Email(message = "Please, provide email!")
        String email,
        @Size(message = "Password length must be at least 8!", min = 8)
        @NotBlank(message = "Password cannot be blank!")
        String password) implements Serializable {
}