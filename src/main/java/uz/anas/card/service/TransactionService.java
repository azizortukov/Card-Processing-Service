package uz.anas.card.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.anas.card.entity.Card;
import uz.anas.card.entity.Transaction;
import uz.anas.card.model.dto.CreateTransactionDto;
import uz.anas.card.repo.TransactionRepository;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public Transaction saveTransaction(CreateTransactionDto transactionDto, long newBalance, Card card, Long exchangeRate) {
        Transaction transaction = Transaction.builder()
                .afterBalance(newBalance)
                .currency(transactionDto.currency())
                .amount(transactionDto.amount())
                .card(card)
                .exchangeRate(exchangeRate)
                .externalId(transactionDto.externalId())
                .purpose(transactionDto.purpose())
                .build();
        return transactionRepository.save(transaction);
    }
}
