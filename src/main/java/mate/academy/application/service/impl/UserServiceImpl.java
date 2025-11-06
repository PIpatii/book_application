package mate.academy.application.service.impl;

import lombok.RequiredArgsConstructor;
import mate.academy.application.dto.user.UserRequestDto;
import mate.academy.application.dto.user.UserResponseDto;
import mate.academy.application.exception.RegistrationException;
import mate.academy.application.mapper.UserMapper;
import mate.academy.application.model.User;
import mate.academy.application.repository.user.UserRepository;
import mate.academy.application.service.UserService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponseDto registerUser(UserRequestDto userRequestDto) {
        if (userRepository.existsUserByEmail(userRequestDto.getEmail())) {
            throw new RegistrationException("Email already exists");
        }
        User user = userMapper.toEntity(userRequestDto);
        userRepository.save(user);

        return userMapper.toResponse(user);
    }
}
