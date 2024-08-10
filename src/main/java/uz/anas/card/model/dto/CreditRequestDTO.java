package uz.anas.card.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import uz.anas.card.entity.enums.Currency;

import java.io.Serializable;

/**
 * DTO for {@link uz.anas.card.entity.Transaction}
 */
public record CreditRequestDTO(@NotNull(message = "External ID cannot be null!") @JsonProperty("external_id") String externalId,
                               @Positive(message = "Transaction cannot be negative or zero!") Long amount,
                               Currency currency) implements Serializable {
    public CreditRequestDTO(String externalId, Long amount, Currency currency) {
        if (currency == null) {
            currency = Currency.UZS;
        }
        this.externalId = externalId;
        this.amount = amount;
        this.currency = currency;
    }
}