package mate.academy.application.mapper;

import mate.academy.application.config.MapperConfig;
import mate.academy.application.dto.user.UserDto;
import mate.academy.application.dto.user.UserRequestDto;
import mate.academy.application.dto.user.UserResponseDto;
import mate.academy.application.model.User;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    UserDto toDto(User user);

    User toEntity(UserRequestDto userRegistrationRequestDto);

    UserResponseDto toResponse(User user);
}
