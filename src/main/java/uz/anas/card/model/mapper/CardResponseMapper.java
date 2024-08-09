package uz.anas.card.model.mapper;

import org.mapstruct.*;
import uz.anas.card.entity.Card;
import uz.anas.card.model.dto.CardResponseDto;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface CardResponseMapper {
    @Mapping(source = "userId", target = "user.id")
    Card toEntity(CardResponseDto cardInfoDto);

    @Mapping(source = "user.id", target = "userId")
    CardResponseDto toDto(Card card);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "userId", target = "user.id")
    Card partialUpdate(CardResponseDto cardInfoDto, @MappingTarget Card card);
}