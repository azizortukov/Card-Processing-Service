package uz.anas.card.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.anas.card.entity.Card;
import uz.anas.card.entity.IdempotencyRecord;
import uz.anas.card.exceptions.BadRequestException;
import uz.anas.card.exceptions.NotFoundException;
import uz.anas.card.model.dto.CreateCardDto;
import uz.anas.card.model.mapper.CardMapper;
import uz.anas.card.model.mapper.CardResponseMapper;
import uz.anas.card.repo.CardRepository;
import uz.anas.card.repo.IdempotencyRecordRepository;
import uz.anas.card.repo.UserRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor

public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;
    private final IdempotencyRecordRepository idempotencyRecordRepository;
    private final CardMapper cardMapper;
    private final UserRepository userRepository;
    private final CardResponseMapper cardResponseMapper;

    @Override
    public HttpEntity<?> createNewCard(UUID idempotencyKey, CreateCardDto cardDto) {
        var record = idempotencyRecordRepository.findById(idempotencyKey);
        if (record.isPresent()) {
            var cardById = cardRepository.findById(record.get().getCardId());
            if (cardById.isPresent()) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(cardResponseMapper.toDto(cardById.get()));
            }
        }
        if (cardRepository.findActiveCardsByUserId(cardDto.userId()).size() == 3) {
            throw new BadRequestException("Card limit is exceeded above 3");
        }

        Card savedCard = cardRepository.save(cardMapper.toEntity(cardDto, userRepository));
        idempotencyRecordRepository.save(new IdempotencyRecord(idempotencyKey, savedCard.getId()));

        return ResponseEntity.status(HttpStatus.CREATED).body(cardResponseMapper.toDto(savedCard));
    }

    @Override
    public HttpEntity<?> getCardById(UUID cardId) {
        var cardById = cardRepository.findById(cardId);
        if (cardById.isEmpty()) {
            throw new NotFoundException("Card with such id not exists in processing.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(cardResponseMapper.toDto(cardById.get()));
    }
}
