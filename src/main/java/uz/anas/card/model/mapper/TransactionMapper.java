package uz.anas.card.model.mapper;

import org.mapstruct.*;
import uz.anas.card.entity.Transaction;
import uz.anas.card.model.dto.response.CreditResponseDTO;
import uz.anas.card.model.dto.response.DebitResponseDTO;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface TransactionMapper {
    @Mapping(source = "cardId", target = "card.id")
    Transaction toEntity(DebitResponseDTO debitResponseDTO);

    @Mapping(source = "card.id", target = "cardId")
    DebitResponseDTO toDebitResponseDto(Transaction transaction);

    @Mapping(source = "card.id", target = "cardId")
    CreditResponseDTO toCreditResponseDto(Transaction transaction);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "cardId", target = "card.id")
    Transaction partialUpdate(DebitResponseDTO debitResponseDTO, @MappingTarget Transaction transaction);
}