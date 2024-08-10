package uz.anas.card.model.mapper;

import org.mapstruct.*;
import uz.anas.card.entity.User;
import uz.anas.card.model.dto.LoginDto;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    User toEntity(LoginDto loginDto);

    LoginDto toDto(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User partialUpdate(LoginDto loginDto, @MappingTarget User user);
}