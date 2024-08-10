package uz.anas.card.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.anas.card.entity.Card;
import uz.anas.card.entity.IdempotencyRecord;
import uz.anas.card.entity.Transaction;
import uz.anas.card.entity.enums.CardStatus;
import uz.anas.card.entity.enums.Currency;
import uz.anas.card.exceptions.BadRequestException;
import uz.anas.card.exceptions.NotFoundException;
import uz.anas.card.model.dto.CardRequestDTO;
import uz.anas.card.model.dto.CreditRequestDTO;
import uz.anas.card.model.dto.DebitRequestDTO;
import uz.anas.card.model.mapper.CardMapper;
import uz.anas.card.model.mapper.CardResponseMapper;
import uz.anas.card.model.mapper.TransactionMapper;
import uz.anas.card.repo.CardRepository;
import uz.anas.card.repo.IdempotencyRecordRepository;
import uz.anas.card.repo.TransactionRepository;
import uz.anas.card.repo.UserRepository;
import uz.anas.card.util.CardUtil;

import java.util.UUID;

@Service
@RequiredArgsConstructor

public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;
    private final IdempotencyRecordRepository idempotencyRecordRepository;
    private final CardMapper cardMapper;
    private final UserRepository userRepository;
    private final CardResponseMapper cardResponseMapper;
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final CardUtil cardUtil;
    private final TransactionService transactionService;

    @Override
    public HttpEntity<?> createNewCard(UUID idempotencyKey, CardRequestDTO cardDto) {
        var recordOptional = idempotencyRecordRepository.findById(idempotencyKey);
        if (recordOptional.isPresent()) {
            var card = cardUtil.checkCardExistence(recordOptional.get().getCardId());
            return ResponseEntity.status(HttpStatus.OK)
                    .body(cardResponseMapper.toDto(card));
        }

        if (cardRepository.findActiveCardsByUserId(cardDto.userId()).size() == 3) {
            throw new BadRequestException("Card limit is exceeded above 3");
        }
        if (userRepository.findById(cardDto.userId()).isEmpty()) {
            throw new NotFoundException("User with provided id is not found");
        }

        Card savedCard = cardRepository.save(cardMapper.toEntity(cardDto, userRepository));
        idempotencyRecordRepository.save(new IdempotencyRecord(idempotencyKey, savedCard.getId()));

        return ResponseEntity.status(HttpStatus.CREATED).body(cardResponseMapper.toDto(savedCard));
    }

    @Override
    public HttpEntity<?> getCardById(UUID cardId) {
        var card = cardUtil.checkCardExistence(cardId);
        return ResponseEntity.status(HttpStatus.OK)
                .eTag(generateETag(card))
                .body(cardResponseMapper.toDto(card));
    }

    @Override
    public HttpEntity<?> blockCard(String eTag, UUID cardId) {
        // Checks the ETag and checks if card in provided ID exists
        Card card = checkEtag(eTag, cardId);

        checkCardStatus(card);
        card.setStatus(CardStatus.BLOCKED);
        cardRepository.save(card);
        return ResponseEntity.noContent().build();
    }

    @Override
    public HttpEntity<?> activeCard(String eTag, UUID cardId) {
        // Checks the ETag and checks if card in provided ID exists
        Card card = checkEtag(eTag, cardId);

        if (!card.getStatus().equals(CardStatus.BLOCKED)) {
            throw new BadRequestException("Card is not blocked.");
        }
        card.setStatus(CardStatus.ACTIVE);
        cardRepository.save(card);
        return ResponseEntity.noContent().build();
    }

    @Override
    public HttpEntity<?> sendMoney(UUID idempotencyKey, UUID cardId, DebitRequestDTO debitDTO) {
        var recordOptional = idempotencyRecordRepository.findById(idempotencyKey);
        if (recordOptional.isPresent()) {
            var transactionOptional = transactionRepository.findById(recordOptional.get().getTransactionId());
            if (transactionOptional.isPresent()) {
                var dto = transactionMapper.toDebitResponseDto(transactionOptional.get());
                return ResponseEntity.ok(dto);
            }
        }
        Card card = cardUtil.checkCardExistence(cardId);
        Transaction transaction = checkBalanceAndWithdraw(card, debitDTO);
        idempotencyRecordRepository.save(new IdempotencyRecord(idempotencyKey, cardId, transaction.getId()));
        return ResponseEntity.ok(transactionMapper.toDebitResponseDto(transaction));
    }

    @Override
    public HttpEntity<?> receiveMoney(UUID idempotencyKey, UUID cardId, CreditRequestDTO creditDTO) {
        var recordOptional = idempotencyRecordRepository.findById(idempotencyKey);
        if (recordOptional.isPresent()) {
            var transactionOptional = transactionRepository.findById(recordOptional.get().getTransactionId());
            if (transactionOptional.isPresent()) {
                var dto = transactionMapper.toCreditResponseDto(transactionOptional.get());
                return ResponseEntity.ok(dto);
            }
        }
        Card card = cardUtil.checkCardExistence(cardId);
        Transaction transaction = addToBalance(card, creditDTO);
        idempotencyRecordRepository.save(new IdempotencyRecord(idempotencyKey, cardId, transaction.getId()));
        return ResponseEntity.ok(transactionMapper.toCreditResponseDto(transaction));
    }

    private Transaction addToBalance(Card card, CreditRequestDTO creditDTO) {
        checkCardStatus(card);
        long sum = creditDTO.amount();
        Long exchangeRate = null;
        if (!creditDTO.currency().equals(card.getCurrency())) {
            exchangeRate = cardUtil.fetchCurrencyRate();
            sum = creditDTO.currency().equals(Currency.USD) ? creditDTO.amount() * exchangeRate : creditDTO.amount() / exchangeRate;
        }
        long newBalance = card.getBalance() + sum;
        card.setBalance(newBalance);
        cardRepository.save(card);
        return transactionService.saveCreditTransaction(creditDTO, newBalance, card, exchangeRate);
    }

    public Transaction checkBalanceAndWithdraw(Card card, DebitRequestDTO debitDTO) {
        checkCardStatus(card);
        // Checks if two currency are same. Then, compares transaction amount and card balance.
        if (debitDTO.currency().equals(card.getCurrency()) && card.getBalance() < debitDTO.amount()) {
            throw new BadRequestException("Insufficient funds.");
        }
        // Checks if currency of card or transaction is different. If yes, then checks the amount.
        if (!debitDTO.currency().equals(card.getCurrency())) {
            long currencyRate = cardUtil.fetchCurrencyRate();
            // Checks the card if it has enough money
            long amountInCardCurrency = cardUtil.sumWithCurrencyRate(card, debitDTO, currencyRate);

            long newBalance = card.getBalance() - amountInCardCurrency;
            card.setBalance(newBalance);
            cardRepository.save(card);
            return transactionService.saveDebitTransaction(debitDTO, newBalance, card, currencyRate);
        }
        // Two currencies are same and sum of transaction is withdrawn from card balance.
        long newBalance = card.getBalance() - debitDTO.amount();
        card.setBalance(newBalance);
        cardRepository.save(card);
        return transactionService.saveDebitTransaction(debitDTO, newBalance, card, null);
    }

    // Checks if card status is ACTIVE for income or outcome
    public void checkCardStatus(Card card) {
        if (!card.getStatus().equals(CardStatus.ACTIVE)) {
            throw new BadRequestException("Card is not active.");
        }
    }

    private Card checkEtag(String eTag, UUID cardId) {
        Card card = cardUtil.checkCardExistence(cardId);
        String tag = generateETag(card);
        if (!tag.equals(eTag)) {
            throw new BadRequestException("ETag does not match.");
        }
        return card;
    }

    private String generateETag(Card card) {
        return "\"" + card.getLastModifiedAt().hashCode() + "\"";
    }

}
