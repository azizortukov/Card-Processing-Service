package uz.anas.card.entity;

import jakarta.persistence.*;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import uz.anas.card.entity.enums.Currency;
import uz.anas.card.entity.enums.TransactionPurpose;
import uz.anas.card.entity.enums.TransactionType;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "transaction")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @NotNull(message = "External ID cannot be null!")
    private String externalId;

    @JoinColumn(name = "card_id")
    @ManyToOne
    private Card card;

    @NotNull(message = "Balance should be provided")
    private Long afterBalance;

    @Positive(message = "Transaction cannot be negative or zero!")
    private Long amount;

    @NotNull(message = "Currency cannot be null!")
    @Enumerated(EnumType.STRING)
    private Currency currency;

    @NotNull(message = "Transaction type cannot be null!")
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @Enumerated(EnumType.STRING)
    private TransactionPurpose purpose;

    private Long exchangeRate;

    @PrePersist
    void prePersist() {
        if (this.transactionType.equals(TransactionType.DEBIT) && this.purpose == null) {
            throw new ConstraintViolationException("Purpose cannot be null!", Set.of());
        }
    }

}