package uz.anas.card.model.mapper;

import org.mapstruct.*;
import uz.anas.card.entity.User;
import uz.anas.card.model.dto.LoginDTO;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    User toEntity(LoginDTO loginDto);

    LoginDTO toDto(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User partialUpdate(LoginDTO loginDto, @MappingTarget User user);
}