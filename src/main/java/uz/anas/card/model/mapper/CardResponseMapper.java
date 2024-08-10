package uz.anas.card.model.mapper;

import org.mapstruct.*;
import uz.anas.card.entity.Card;
import uz.anas.card.model.dto.ResponseCardDto;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface CardResponseMapper {
    @Mapping(source = "userId", target = "user.id")
    Card toEntity(ResponseCardDto cardInfoDto);

    @Mapping(source = "user.id", target = "userId")
    ResponseCardDto toDto(Card card);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "userId", target = "user.id")
    Card partialUpdate(ResponseCardDto cardInfoDto, @MappingTarget Card card);
}