package mate.academy.application.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mate.academy.application.dto.user.UserLoginRequestDto;
import mate.academy.application.dto.user.UserLoginResponseDto;
import mate.academy.application.dto.user.UserRequestDto;
import mate.academy.application.dto.user.UserResponseDto;
import mate.academy.application.exception.RegistrationException;
import mate.academy.application.service.UserService;
import mate.academy.application.service.impl.AuthenticationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User", description = "registration of users")
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @PostMapping("/registration")
    @Operation(summary = "Registration", description = "Registration of user")
    public UserResponseDto register(@RequestBody @Valid UserRequestDto request)
            throws RegistrationException {
        return userService.registerUser(request);
    }

    @PostMapping("/login")
    @Operation(summary = "Login", description = "Log in existing account")
    public UserLoginResponseDto login(@RequestBody @Valid UserLoginRequestDto request) {
        return authenticationService.login(request);
    }
}
