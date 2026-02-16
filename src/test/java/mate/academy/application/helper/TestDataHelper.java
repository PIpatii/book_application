package mate.academy.application.helper;

import mate.academy.application.dto.book.BookDto;
import mate.academy.application.dto.book.CreateBookRequestDto;
import mate.academy.application.dto.cart.ShoppingCartDto;
import mate.academy.application.model.CartItem;
import mate.academy.application.model.ShoppingCart;
import mate.academy.application.model.User;

import java.math.BigDecimal;
import java.util.Set;

public final class TestDataHelper {

    private TestDataHelper() {
    }

    public static BookDto createBookDto(
            Long id,
            String author,
            String title,
            String isbn,
            BigDecimal price,
            String description,
            String coverImage,
            Set<Long> categoryIds
    ) {
        BookDto dto = new BookDto();
        dto.setId(id);
        dto.setAuthor(author);
        dto.setTitle(title);
        dto.setIsbn(isbn);
        dto.setPrice(price);
        dto.setDescription(description);
        dto.setCoverImage(coverImage);
        dto.setCategoryIds(categoryIds);

        return dto;
    }

    public static CreateBookRequestDto createBookRequestDto(
            String author,
            String title,
            String isbn,
            BigDecimal price,
            String description,
            String coverImage,
            Set<Long> categoryIds
    ) {
        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setAuthor(author);
        requestDto.setTitle(title);
        requestDto.setIsbn(isbn);
        requestDto.setPrice(price);
        requestDto.setDescription(description);
        requestDto.setCoverImage(coverImage);
        requestDto.setCategoryIds(categoryIds);

        return requestDto;
    }

    public static User createUser(Long id, String firstName, String lastName, String email, String password) {
        User user = new User();
        user.setId(id);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(password);

        return user;
    }

    public static ShoppingCart createShoppingCart(Long Id, User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setId(Id);
        shoppingCart.setUser(user);

        return shoppingCart;
    }

    public static ShoppingCartDto createShoppingCartDto(Long Id, Long userId) {
        ShoppingCartDto shoppingCartDto = new ShoppingCartDto();
        shoppingCartDto.setId(Id);
        shoppingCartDto.setUserId(userId);

        return shoppingCartDto;
    }

    public static CartItem createCartItem(Long Id, ShoppingCart shoppingCart) {
        CartItem cartItem = new CartItem();
        cartItem.setId(Id);
        cartItem.setShoppingCart(shoppingCart);

        return cartItem;
    }


}
