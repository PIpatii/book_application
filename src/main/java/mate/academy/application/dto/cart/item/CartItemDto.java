package mate.academy.application.dto.cart.item;

import lombok.Getter;
import lombok.Setter;
import mate.academy.application.model.Book;
import mate.academy.application.model.ShoppingCart;

@Getter
@Setter
public class CartItemDto {
    private Long id;
    private ShoppingCart shoppingCart;
    private Book book;
    private int quantity;

}
