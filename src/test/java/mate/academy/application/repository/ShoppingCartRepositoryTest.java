package mate.academy.application.repository;

import mate.academy.application.model.ShoppingCart;
import mate.academy.application.model.User;
import mate.academy.application.repository.cart.ShoppingCartRepository;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import static mate.academy.application.helper.TestDataHelper.createShoppingCart;
import static mate.academy.application.helper.TestDataHelper.createUser;

@DataJpaTest
@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ShoppingCartRepositoryTest {
    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Test
    @Sql(scripts = "classpath:database/cart/add-elements-to-each-table.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/cart/remove-from-each-table.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getShoppingCartByUserId() {
        User user = createUser(2L, "first_name",
                "last_name", "email", "password");
        ShoppingCart excpectedShoppingCart = createShoppingCart(2L, user);

        ShoppingCart actual = shoppingCartRepository.getShoppingCartByUserId(2L);
        Assertions.assertEquals(excpectedShoppingCart.getId(), actual.getId());
        Assertions.assertEquals(excpectedShoppingCart.getUser().getEmail(), actual.getUser().getEmail());
    }
}
