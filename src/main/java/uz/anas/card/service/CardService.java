package uz.anas.card.service;

import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import uz.anas.card.model.dto.CreateCardDto;
import uz.anas.card.model.dto.CreateTransactionDto;

import java.util.UUID;

@Service
public interface CardService {
    HttpEntity<?> createNewCard(UUID idempotencyKey, CreateCardDto cardDto);

    HttpEntity<?> getCardById(UUID cardId);

    HttpEntity<?> blockCard(String eTag, UUID cardId);

    HttpEntity<?> activeCard(String eTag, UUID cardId);

    HttpEntity<?> sendMoney(UUID idempotencyKey, UUID cardId, CreateTransactionDto createTransactionDto);
}
