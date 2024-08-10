package uz.anas.card.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.anas.card.entity.Card;
import uz.anas.card.entity.Transaction;
import uz.anas.card.entity.enums.TransactionType;
import uz.anas.card.model.dto.CreditRequestDTO;
import uz.anas.card.model.dto.DebitRequestDTO;
import uz.anas.card.repo.TransactionRepository;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public Transaction saveDebitTransaction(DebitRequestDTO debitDTO, long newBalance, Card card, Long exchangeRate) {
        Transaction transaction = Transaction.builder()
                .afterBalance(newBalance)
                .transactionType(TransactionType.DEBIT)
                .currency(debitDTO.currency())
                .amount(debitDTO.amount())
                .card(card)
                .exchangeRate(exchangeRate)
                .externalId(debitDTO.externalId())
                .purpose(debitDTO.purpose())
                .build();
        return transactionRepository.save(transaction);
    }

    public Transaction saveCreditTransaction(CreditRequestDTO creditDTO, long newBalance, Card card, Long exchangeRate) {
        Transaction transaction = Transaction.builder()
                .afterBalance(newBalance)
                .transactionType(TransactionType.CREDIT)
                .currency(creditDTO.currency())
                .amount(creditDTO.amount())
                .card(card)
                .exchangeRate(exchangeRate)
                .externalId(creditDTO.externalId())
                .build();
        return transactionRepository.save(transaction);
    }
}
