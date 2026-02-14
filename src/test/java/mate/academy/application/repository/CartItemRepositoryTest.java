package mate.academy.application.repository;

import mate.academy.application.model.CartItem;
import mate.academy.application.model.ShoppingCart;
import mate.academy.application.model.User;
import mate.academy.application.repository.cart.CartItemRepository;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.Optional;
import static mate.academy.application.helper.TestDataHelper.createCartItem;
import static mate.academy.application.helper.TestDataHelper.createShoppingCart;
import static mate.academy.application.helper.TestDataHelper.createUser;

@DataJpaTest
@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CartItemRepositoryTest {
    @Autowired
    private CartItemRepository cartItemRepository;

    @Test
    @Sql(scripts = "classpath:database/cart/add-elements-to-each-table.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/cart/remove-from-each-table.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findByIdAndShoppingCartId() {
        User user = createUser(2L, "first_name",
                "last_name", "email", "password");

        ShoppingCart excpectedShoppingCart = createShoppingCart(2L, user);

        CartItem cartItem = createCartItem(1L, excpectedShoppingCart);

        Optional<CartItem> actual = cartItemRepository.findByIdAndShoppingCartId(cartItem.getId(),
                excpectedShoppingCart.getId());

        Assertions.assertEquals(cartItem.getId(), actual.map(CartItem::getId).orElse(null));
        Assertions.assertEquals(cartItem.getShoppingCart().getUser().getEmail(),
                actual.map(item -> item.getShoppingCart().getUser().getEmail()).orElse(null));
    }
}
