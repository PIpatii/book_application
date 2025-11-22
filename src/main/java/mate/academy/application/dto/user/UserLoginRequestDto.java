package mate.academy.application.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLoginRequestDto {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
}
