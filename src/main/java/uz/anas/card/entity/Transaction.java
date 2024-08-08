package uz.anas.card.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import uz.anas.card.entity.enums.Currency;
import uz.anas.card.entity.enums.TransactionPurpose;

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
    @Positive(message = "Transaction cannot be negative!")
    private Long amount;
    @NotNull(message = "Currency cannot be null!")
    @Enumerated
    private Currency currency;
    @NotNull(message = "Purpose cannot be null!")
    @Enumerated
    private TransactionPurpose purpose;

}