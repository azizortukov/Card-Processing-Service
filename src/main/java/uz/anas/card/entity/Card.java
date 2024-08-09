package uz.anas.card.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import uz.anas.card.entity.enums.CardStatus;
import uz.anas.card.entity.enums.Currency;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "card")
@EntityListeners(AuditingEntityListener.class)
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @JsonProperty("user_id")
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull(message = "Card status cannot be null!")
    @Enumerated(EnumType.STRING)
    private CardStatus status;

    @PositiveOrZero(message = "Balance cannot be negative!")
    @NotNull(message = "Balance cannot be null!")
    private Long balance;

    @NotNull(message = "Currency cannot be null!")
    @Enumerated(EnumType.STRING)
    private Currency currency;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime lastModifiedAt;

}