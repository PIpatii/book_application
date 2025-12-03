package mate.academy.application.repository.cart;

import mate.academy.application.model.ShoppingCart;
import mate.academy.application.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {

    ShoppingCart getShoppingCartByUser(User user);
}
