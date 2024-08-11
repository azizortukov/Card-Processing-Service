package uz.anas.card.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import uz.anas.card.entity.Card;
import uz.anas.card.entity.IdempotencyRecord;
import uz.anas.card.entity.User;
import uz.anas.card.entity.enums.CardStatus;
import uz.anas.card.entity.enums.Currency;
import uz.anas.card.exceptions.BadRequestException;
import uz.anas.card.exceptions.NotFoundException;
import uz.anas.card.model.dto.request.CardRequestDTO;
import uz.anas.card.model.dto.response.CardResponseDTO;
import uz.anas.card.model.mapper.CardMapper;
import uz.anas.card.model.mapper.CardResponseMapper;
import uz.anas.card.model.mapper.TransactionMapper;
import uz.anas.card.repo.CardRepository;
import uz.anas.card.repo.IdempotencyRecordRepository;
import uz.anas.card.repo.TransactionRepository;
import uz.anas.card.repo.UserRepository;
import uz.anas.card.util.CardUtil;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardServiceImplTest {

    @Mock
    private CardRepository cardRepository;
    @Mock
    private IdempotencyRecordRepository idempotencyRecordRepository;
    @Mock
    private CardMapper cardMapper;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CardResponseMapper cardResponseMapper;
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private TransactionMapper transactionMapper;
    @Mock
    private CardUtil cardUtil;
    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private CardServiceImpl cardService;

    private final CardRequestDTO cardRequestDTO = new CardRequestDTO(2L, CardStatus.ACTIVE, 200L, Currency.UZS);;
    private final User user = User.builder().id(cardRequestDTO.userId()).build();;
    private final Card card = new Card(UUID.randomUUID(), user, cardRequestDTO.status(), cardRequestDTO.balance(), cardRequestDTO.currency(), null, null);;
    private final CardResponseDTO cardResponseDTO = new CardResponseDTO(card.getId(), card.getUser().getId(), card.getStatus(), card.getBalance(), card.getCurrency());

    @Test
    void createNewCardIdempotencyKeyExits() {
        when(idempotencyRecordRepository.findById(any()))
                .thenReturn(Optional.of(new IdempotencyRecord()));
        when(cardUtil.checkCardExistence(any()))
                .thenReturn(card);
        when(cardResponseMapper.toDto(any(Card.class)))
                .thenReturn(cardResponseDTO);

        var response = (ResponseEntity<?>) cardService.createNewCard(UUID.randomUUID(), cardRequestDTO);
        CardResponseDTO responseDTO = (CardResponseDTO) response.getBody();

        assertNotNull(response);
        assertNotNull(responseDTO);
        assertEquals(cardRequestDTO.status(), responseDTO.status());
        assertEquals(cardRequestDTO.userId(), responseDTO.userId());
        assertEquals(cardRequestDTO.balance(), responseDTO.balance());
        assertEquals(cardRequestDTO.currency(), responseDTO.currency());
    }

    @Test
    void createNewCardOverMaxCardCount() {
        when(cardRepository.findActiveCardsByUserId(any()))
                .thenReturn(3);
        assertThrows(BadRequestException.class, () -> cardService.createNewCard(UUID.randomUUID(), cardRequestDTO));
    }

    @Test
    void createNewCardUserByIdNotFound() {
        assertThrows(NotFoundException.class, () -> cardService.createNewCard(UUID.randomUUID(), cardRequestDTO));
    }

    @Test
    void createNewCard() {
        when(cardMapper.toEntity(any(CardRequestDTO.class), any(UserRepository.class)))
                .thenReturn(card);
        when(cardRepository.save(any(Card.class)))
                .thenReturn(card);
        when(userRepository.findById(any()))
                .thenReturn(Optional.of(user));
        when(cardResponseMapper.toDto(any(Card.class)))
                .thenReturn(cardResponseDTO);

        // Perform the actual method call
        var resp = (ResponseEntity<?>) cardService.createNewCard(UUID.randomUUID(), cardRequestDTO);

        // Validate the response
        assertNotNull(resp);
        assertNotNull(resp.getBody());
        assertEquals(cardResponseDTO, resp.getBody());

        // Verify the interactions with the mocks
        verify(cardRepository, times(1)).save(any(Card.class));
        verify(cardMapper, times(1)).toEntity(any(CardRequestDTO.class), any(UserRepository.class));
        verify(idempotencyRecordRepository, times(1)).save(any(IdempotencyRecord.class));
    }

    @Test
    void getCardById() {
    }

    @Test
    void blockCard() {
    }

    @Test
    void activeCard() {
    }

    @Test
    void sendMoney() {
    }

    @Test
    void receiveMoney() {
    }

    @Test
    void checkBalanceAndWithdraw() {
    }

    @Test
    void checkCardStatus() {
    }
}