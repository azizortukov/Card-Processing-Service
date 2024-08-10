package uz.anas.card.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import uz.anas.card.entity.Card;
import uz.anas.card.entity.enums.Currency;
import uz.anas.card.exceptions.BadRequestException;
import uz.anas.card.exceptions.InternalServerException;
import uz.anas.card.exceptions.NotFoundException;
import uz.anas.card.model.dto.request.DebitRequestDTO;
import uz.anas.card.model.dto.CurrencyRateDTO;
import uz.anas.card.repo.CardRepository;

import java.util.List;
import java.util.UUID;

@EnableScheduling
@Service
@RequiredArgsConstructor
public class CardUtil {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final CardRepository cardRepository;


    @Cacheable(value = "currencyRate")
    public long fetchCurrencyRate() {
        String url = "https://cbu.uz/uz/arkhiv-kursov-valyut/json/USD/";
        String jsonResponse = restTemplate.getForObject(url, String.class);

        try {
            if (jsonResponse != null && !jsonResponse.isEmpty()) {
                List<CurrencyRateDTO> rates = objectMapper.readValue(jsonResponse, new TypeReference<>() {
                });
                if (rates.isEmpty()) {
                    throw new InternalServerException("The currency could not be loaded.");
                }
                return Math.round(rates.get(0).rate());
            }
            throw new InternalServerException("The currency could not be loaded.");
        } catch (JsonProcessingException e) {
            throw new InternalServerException("The currency could not be parsed.");
        }

    }

    @CacheEvict(value = "currencyRate", allEntries = true)
    @Scheduled(cron = "0 0 1 * * *")
    public void clearCache() {}

    public long sumWithCurrencyRate(Card card, DebitRequestDTO transactionDto, long currencyRate) {
        long sum;
        if (transactionDto.currency().equals(Currency.USD)) {
            sum = transactionDto.amount() * currencyRate;
            if (sum > card.getBalance()) {
                throw new BadRequestException("Insufficient funds.");
            }
            return sum;
        } else {
            sum = transactionDto.amount() / currencyRate;
            if (sum > card.getBalance()) {
                throw new BadRequestException("Insufficient funds.");
            }
            return sum;
        }
    }

    public Card checkCardExistence(UUID cardId) {
        var cardOptional = cardRepository.findById(cardId);
        if (cardOptional.isEmpty()) {
            throw new NotFoundException("Card with such id not exists in processing.");
        }
        return cardOptional.get();
    }

}
