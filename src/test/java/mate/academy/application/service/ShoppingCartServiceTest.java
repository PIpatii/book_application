package mate.academy.application.service;

import mate.academy.application.dto.cart.ShoppingCartDto;
import mate.academy.application.dto.cart.item.AddCartItemRequestDto;
import mate.academy.application.dto.cart.item.CartItemResponseDto;
import mate.academy.application.dto.cart.item.UpdateCartItemRequestDto;
import mate.academy.application.mapper.CartItemsMapper;
import mate.academy.application.mapper.ShoppingCartMapper;
import mate.academy.application.model.Book;
import mate.academy.application.model.CartItem;
import mate.academy.application.model.ShoppingCart;
import mate.academy.application.model.User;
import mate.academy.application.repository.book.BookRepository;
import mate.academy.application.repository.cart.CartItemRepository;
import mate.academy.application.repository.cart.ShoppingCartRepository;
import mate.academy.application.repository.user.UserRepository;
import mate.academy.application.service.impl.ShoppingCartServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import static mate.academy.application.helper.TestDataHelper.*;
import static mate.academy.application.helper.TestSecurityUtils.mockAuth;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class ShoppingCartServiceTest {

    @InjectMocks
    private ShoppingCartServiceImpl shoppingCartService;
    @Mock
    private ShoppingCartRepository shoppingCartRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CartItemRepository cartItemRepository;
    @Mock
    private ShoppingCartMapper shoppingCartMapper;
    @Mock
    private CartItemsMapper cartItemsMapper;
    @Mock
    private BookRepository bookRepository;

    @Test
    @DisplayName("get a shopping cart")
    public void getShoppingCart() {
        mockAuth("email");

        User user = createUser(1L, "firstName", "lastName",
                "email", "password");

        ShoppingCart shoppingCart = createShoppingCart(1L, user);

        ShoppingCartDto  expectedDto = createShoppingCartDto(1L, user.getId());

        Mockito.when(shoppingCartRepository.getShoppingCartByUserId(user.getId())).thenReturn(shoppingCart);
        Mockito.when(userRepository.getUserByEmail("email")).thenReturn(user);
        Mockito.when(shoppingCartMapper.toDto(shoppingCart)).thenReturn(expectedDto);

        ShoppingCartDto actual = shoppingCartService.getShoppingCart();

        Assertions.assertEquals(expectedDto, actual);
    }

    @Test
    @DisplayName("save an item to a shopping cart")
    public void saveItem() {
        mockAuth("email");

        Book bookKobzar = new Book();
        bookKobzar.setId(1L);

        User user = createUser(1L, "first_name", "last_name",
                "email", "password");

        ShoppingCart shoppingCart = createShoppingCart(1L, user);

        CartItem cartItem = createCartItem(10L, shoppingCart);

        Set<CartItem> cartItems = new HashSet<>();
        cartItems.add(cartItem);
        shoppingCart.setCartItems(cartItems);

        ShoppingCartDto expectedDto = new ShoppingCartDto();
        expectedDto.setId(100L);

        AddCartItemRequestDto newItem = new AddCartItemRequestDto();
        newItem.setBookId(bookKobzar.getId());
        newItem.setQuantity(1);

        Set<CartItemResponseDto> mappedItems = new HashSet<>();

        Mockito.when(userRepository.getUserByEmail("email"))
                .thenReturn(user);
        Mockito.when(shoppingCartRepository.getShoppingCartByUserId(user.getId()))
                .thenReturn(shoppingCart);
        Mockito.when(bookRepository.findBookById(bookKobzar.getId())).thenReturn(bookKobzar);
        Mockito.when(cartItemsMapper.addCartRequestToModel(newItem)).thenReturn(cartItem);
        Mockito.when(shoppingCartMapper.toDto(shoppingCart)).thenReturn(expectedDto);
        Mockito.when(cartItemsMapper.toDtoSet(cartItems)).thenReturn(mappedItems);

        ShoppingCartDto actual = shoppingCartService.saveItem(newItem);

        assertEquals(expectedDto, actual);

    }

    @Test
    @DisplayName("update the quantity of an item")
    public void updateQuantity() {
        mockAuth("email");

        User user = createUser(1L, "first_name", "last_name",
                "email", "password");

        ShoppingCart shoppingCart = createShoppingCart(1L, user);

        CartItem cartItem = createCartItem(10L, shoppingCart);

        Set<CartItem> cartItems = new HashSet<>();
        cartItems.add(cartItem);
        shoppingCart.setCartItems(cartItems);

        UpdateCartItemRequestDto requestDto = new UpdateCartItemRequestDto();
        requestDto.setQuantity(5);

        ShoppingCartDto expectedDto = new ShoppingCartDto();
        expectedDto.setId(100L);

        Set<CartItemResponseDto> mappedItems = new HashSet<>();

        CartItemResponseDto updatedItemDto = new CartItemResponseDto();

        Mockito.when(userRepository.getUserByEmail("email"))
                .thenReturn(user);

        Mockito.when(shoppingCartRepository.getShoppingCartByUserId(user.getId()))
                .thenReturn(shoppingCart);

        Mockito.when(cartItemRepository.findByIdAndShoppingCartId(cartItem.getId(), 1L))
                .thenReturn(Optional.of(cartItem));
        Mockito.when(shoppingCartMapper.toDto(shoppingCart)).thenReturn(expectedDto);
        Mockito.when(cartItemsMapper.toDtoSet(cartItems)).thenReturn(mappedItems);
        Mockito.when(cartItemsMapper.toResponseDto(cartItem)).thenReturn(updatedItemDto);

        ShoppingCartDto actual = shoppingCartService.updateItem(requestDto, cartItem.getId());

        Mockito.verify(cartItemsMapper).updateItemFromDto(requestDto, cartItem);
        Mockito.verify(cartItemRepository).save(cartItem);
        Mockito.verify(shoppingCartRepository).save(shoppingCart);

        Assertions.assertEquals(expectedDto, actual);
    }

    @Test
    @DisplayName("delete an item")
    public void deleteItem() {
        mockAuth("email");

        User user = createUser(1L, "firstName", "lastName",
                "email", "password");

        ShoppingCart shoppingCart = createShoppingCart(1L, user);

        CartItem cartItem = createCartItem(1L, shoppingCart);

        Mockito.when(userRepository.getUserByEmail("email")).thenReturn(user);
        Mockito.when(shoppingCartRepository.getShoppingCartByUserId(user.getId())).thenReturn(shoppingCart);
        Mockito.when(cartItemRepository.findByIdAndShoppingCartId(1L, shoppingCart.getId()))
                .thenReturn(Optional.of(cartItem));
        
        shoppingCartService.deleteItem(cartItem.getId());
        Mockito.verify(cartItemRepository).delete(cartItem);
    }
}
