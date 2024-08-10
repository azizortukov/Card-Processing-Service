package uz.anas.card.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uz.anas.card.entity.Card;
import uz.anas.card.entity.Transaction;
import uz.anas.card.entity.enums.Currency;
import uz.anas.card.entity.enums.TransactionType;
import uz.anas.card.model.dto.CreditRequestDTO;
import uz.anas.card.model.dto.DebitRequestDTO;
import uz.anas.card.repo.TransactionRepository;
import uz.anas.card.util.CardUtil;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CardUtil cardUtil;

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


    public Page<?> getTransactions(UUID cardId, TransactionType type, UUID transactionId, String externalId, Currency currency, int page, int size) {
        cardUtil.checkCardExistence(cardId);
        Pageable pageable = PageRequest.of(page, size);
        String transactionType = type != null ? type.name() : null;
        String currencyParam = currency != null ? currency.name() : null;
        return transactionRepository.findAll(cardId, transactionType, transactionId, externalId, currencyParam, pageable);
    }
}
