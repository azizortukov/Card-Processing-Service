package uz.anas.card.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import uz.anas.card.entity.enums.Currency;
import uz.anas.card.entity.enums.TransactionPurpose;

import java.io.Serializable;

/**
 * DTO for {@link uz.anas.card.entity.Transaction}
 */
public record CreateTransactionDto(@NotNull(message = "External ID cannot be null!") @JsonProperty("external_id") String externalId,
                                   @Positive(message = "Transaction cannot be negative!") Long amount,
                                   Currency currency,
                                   @NotNull(message = "Purpose cannot be null!") TransactionPurpose purpose) implements Serializable {
    public CreateTransactionDto(String externalId, Long amount, Currency currency, TransactionPurpose purpose) {
        if (currency == null) {
            currency = Currency.UZS;
        }
        this.externalId = externalId;
        this.amount = amount;
        this.currency = currency;
        this.purpose = purpose;
    }
}