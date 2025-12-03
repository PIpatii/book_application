package mate.academy.application.dto.cart.item;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddCartItemRequestDto {
    @NotNull
    private Long bookId;
    @NotNull
    private int quantity;
}
