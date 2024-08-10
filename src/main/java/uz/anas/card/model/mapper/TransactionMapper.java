package uz.anas.card.model.mapper;

import org.mapstruct.*;
import uz.anas.card.entity.Transaction;
import uz.anas.card.model.dto.ResponseTransactionDto;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface TransactionMapper {
    @Mapping(source = "cardId", target = "card.id")
    Transaction toEntity(ResponseTransactionDto responseTransactionDto);

    @Mapping(source = "card.id", target = "cardId")
    ResponseTransactionDto toDto(Transaction transaction);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "cardId", target = "card.id")
    Transaction partialUpdate(ResponseTransactionDto responseTransactionDto, @MappingTarget Transaction transaction);
}