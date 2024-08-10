package uz.anas.card.model.mapper;

import org.mapstruct.*;
import uz.anas.card.entity.Card;
import uz.anas.card.model.dto.response.CardResponseDTO;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface CardResponseMapper {
    @Mapping(source = "userId", target = "user.id")
    Card toEntity(CardResponseDTO cardInfoDto);

    @Mapping(source = "user.id", target = "userId")
    CardResponseDTO toDto(Card card);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "userId", target = "user.id")
    Card partialUpdate(CardResponseDTO cardInfoDto, @MappingTarget Card card);
}