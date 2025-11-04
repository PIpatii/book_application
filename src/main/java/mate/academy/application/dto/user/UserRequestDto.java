package mate.academy.application.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import mate.academy.application.annotation.FieldMatch;

@Getter
@Setter
@FieldMatch(first = "password", second = "repeatPassword", message = "Passwords must match")
public class UserRequestDto {
    @NotBlank
    private String email;
    @NotBlank
    private String password;
    @NotBlank
    private String repeatPassword;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    private String shippingAddress;
}
