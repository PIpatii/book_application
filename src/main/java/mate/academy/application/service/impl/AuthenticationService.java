package mate.academy.application.service.impl;

import lombok.RequiredArgsConstructor;
import mate.academy.application.dto.user.UserLoginRequestDto;
import mate.academy.application.dto.user.UserLoginResponseDto;
import mate.academy.application.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthenticationService {

    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public UserLoginResponseDto login(UserLoginRequestDto request) {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(),
                        request.getPassword()));
        String token = jwtUtil.generateToken(authentication.getName());

        return new UserLoginResponseDto(token);
    }
}
