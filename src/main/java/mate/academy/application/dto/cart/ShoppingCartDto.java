package mate.academy.application.dto.cart;

import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import mate.academy.application.dto.cart.item.CartItemResponseDto;

@Getter
@Setter
public class ShoppingCartDto {
    private Long id;
    private Long userId;
    private Set<CartItemResponseDto> cartItems;
}
