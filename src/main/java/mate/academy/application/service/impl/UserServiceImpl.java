package mate.academy.application.service.impl;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import mate.academy.application.dto.user.UserRequestDto;
import mate.academy.application.dto.user.UserResponseDto;
import mate.academy.application.exception.RegistrationException;
import mate.academy.application.mapper.UserMapper;
import mate.academy.application.model.Role;
import mate.academy.application.model.User;
import mate.academy.application.repository.role.RoleRepository;
import mate.academy.application.repository.user.UserRepository;
import mate.academy.application.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDto registerUser(UserRequestDto userRequestDto) {
        if (userRepository.existsUserByEmail(userRequestDto.getEmail())) {
            throw new RegistrationException("Email "
                    + userRequestDto.getEmail()
                    + " already exists");
        }
        User user = userMapper.toEntity(userRequestDto);
        user.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
        Role defaultRole = roleRepository.findByName(Role.RoleName.USER);
        user.setRoles(Set.of(defaultRole));
        userRepository.save(user);

        return userMapper.toResponse(user);
    }
}
