package uz.anas.card.model.projection;

import com.fasterxml.jackson.annotation.JsonProperty;
import uz.anas.card.entity.enums.Currency;
import uz.anas.card.entity.enums.TransactionPurpose;
import uz.anas.card.entity.enums.TransactionType;

import java.util.UUID;

public interface TransactionProjection {

    @JsonProperty("transaction_id")
    UUID getId();

    @JsonProperty("external_id")
    String getExternalId();

    @JsonProperty("card_id")
    UUID getCardId();

    Long getAmount();

    @JsonProperty("after_balance")
    Long getAfterBalance();

    Currency getCurrency();

    TransactionType getTransactionType();

    TransactionPurpose getPurpose();

    @JsonProperty("exchange_rate")
    Long getExchangeRate();
}
