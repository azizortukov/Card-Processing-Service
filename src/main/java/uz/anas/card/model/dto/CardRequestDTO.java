package uz.anas.card.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import uz.anas.card.entity.enums.CardStatus;
import uz.anas.card.entity.enums.Currency;

import java.io.Serializable;

/**
 * DTO for {@link uz.anas.card.entity.Card}
 */
public record CardRequestDTO(

        @JsonProperty("user_id")
        @NotNull(message = "User id cannot be null")
        Long userId,
        CardStatus status,

        @JsonProperty("initial_amount")
        @PositiveOrZero(message = "Balance cannot be negative!")
        @Max(value = 10_000, message = "Max initial balance can be up to 10 000")
        Long balance,

        Currency currency) implements Serializable {

    public CardRequestDTO(Long userId, CardStatus status, Long balance, Currency currency) {
        this.userId = userId;
        this.status = status != null ? status : CardStatus.ACTIVE;
        this.balance = balance != null ? balance : 0;
        this.currency = currency != null ? currency : Currency.UZS;
    }

}