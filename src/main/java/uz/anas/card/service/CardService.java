package uz.anas.card.service;

import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import uz.anas.card.model.dto.CardRequestDTO;
import uz.anas.card.model.dto.CreditRequestDTO;
import uz.anas.card.model.dto.DebitRequestDTO;

import java.util.UUID;

@Service
public interface CardService {
    HttpEntity<?> createNewCard(UUID idempotencyKey, CardRequestDTO cardDto);

    HttpEntity<?> getCardById(UUID cardId);

    HttpEntity<?> blockCard(String eTag, UUID cardId);

    HttpEntity<?> activeCard(String eTag, UUID cardId);

    HttpEntity<?> sendMoney(UUID idempotencyKey, UUID cardId, DebitRequestDTO debitRequestDTO);

    HttpEntity<?> receiveMoney(UUID idempotencyKey, UUID cardId, CreditRequestDTO creditRequestDTO);
}
