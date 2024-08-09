package uz.anas.card.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import uz.anas.card.entity.enums.CardStatus;
import uz.anas.card.entity.enums.Currency;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link uz.anas.card.entity.Card}
 */
public record CardResponseDto(UUID id, Long userId, @NotNull(message = "Card status cannot be null!") CardStatus status,
                          @NotNull(message = "Balance cannot be null!") @PositiveOrZero(message = "Balance cannot be negative!") Long balance,
                          @NotNull(message = "Currency cannot be null!") Currency currency) implements Serializable {
}