package uz.anas.card.model.mapper;

import org.mapstruct.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import uz.anas.card.entity.Card;
import uz.anas.card.entity.User;
import uz.anas.card.model.dto.CreateCardDto;
import uz.anas.card.repo.UserRepository;


@Mapper( unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = UserMapper.class)
public interface CardMapper {

    @Mapping(target = "user", source = "userId")
    Card toEntity(CreateCardDto createCardDto, @Context UserRepository userRepository);

    CreateCardDto toDto(Card card);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Card partialUpdate(CreateCardDto createCardDto, @MappingTarget Card card);

    default User mapUser(Long userId, @Context UserRepository userRepository) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + userId));
    }

}