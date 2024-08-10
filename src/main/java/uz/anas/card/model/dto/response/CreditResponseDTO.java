package uz.anas.card.model.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import uz.anas.card.entity.enums.Currency;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link uz.anas.card.entity.Transaction}
 */
public record CreditResponseDTO(
        @JsonProperty("transaction_id") UUID id,
        @NotNull(message = "External ID cannot be null!") @JsonProperty("external_id") String externalId,
        @NotNull(message = "Card ID cannot be null") @JsonProperty("card_id") UUID cardId,
        @NotNull(message = "Balance should be provided") @JsonProperty("after_balance") Long afterBalance,
        @Positive(message = "Transaction cannot be negative or zero!") Long amount,
        @NotNull(message = "Currency cannot be null!") Currency currency,
        Long exchangeRate) implements Serializable {
}