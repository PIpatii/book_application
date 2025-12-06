package mate.academy.application.service.impl;

import jakarta.persistence.EntityNotFoundException;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import mate.academy.application.dto.cart.ShoppingCartDto;
import mate.academy.application.dto.cart.item.AddCartItemRequestDto;
import mate.academy.application.dto.cart.item.CartItemResponseDto;
import mate.academy.application.dto.cart.item.UpdateCartItemRequestDto;
import mate.academy.application.mapper.CartItemsMapper;
import mate.academy.application.mapper.ShoppingCartMapper;
import mate.academy.application.model.CartItem;
import mate.academy.application.model.ShoppingCart;
import mate.academy.application.model.User;
import mate.academy.application.repository.book.BookRepository;
import mate.academy.application.repository.cart.ShoppingCartRepository;
import mate.academy.application.repository.item.CartItemRepository;
import mate.academy.application.repository.user.UserRepository;
import mate.academy.application.service.ShoppingCartService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final CartItemsMapper cartItemsMapper;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    @Override
    public ShoppingCartDto getShoppingCart() {
        ShoppingCart shoppingCart = shoppingCartRepository.getShoppingCartByUserId(getUserId());

        return getResponseDto(shoppingCart, shoppingCart.getCartItems());
    }

    @Override
    public ShoppingCartDto saveItem(AddCartItemRequestDto requestDto) {
        ShoppingCart shoppingCart = shoppingCartRepository.getShoppingCartByUserId(getUserId());

        CartItem cartItem = cartItemsMapper.addCartRequestToModel(requestDto);
        cartItem.setBook(bookRepository.findBooksById(requestDto.getBookId()));
        cartItem.setShoppingCart(shoppingCart);
        Set<CartItem> cartItems = shoppingCart.getCartItems();

        boolean exists = cartItems.stream()
                .anyMatch(item -> item.getBook() != null
                        && cartItem.getBook().equals(item.getBook()));

        if (exists) {
            cartItems.stream()
                    .filter(item -> item.getBook().equals(cartItem.getBook()))
                    .forEach(item -> item.setQuantity(item.getQuantity()
                            + cartItem.getQuantity()));

            return getResponseDto(shoppingCart, cartItems);
        } else {
            cartItemRepository.save(cartItem);

            cartItems.add(cartItem);
            shoppingCart.setCartItems(cartItems);
            shoppingCartRepository.save(shoppingCart);
        }

        return getResponseDtoWithNewItem(shoppingCart, cartItem, cartItems);
    }

    @Override
    public ShoppingCartDto updateItem(UpdateCartItemRequestDto requestDto, Long id) {
        ShoppingCart shoppingCart = shoppingCartRepository.getShoppingCartByUserId(getUserId());

        CartItem cartItem = cartItemRepository.findByIdAndShoppingCartId(id, shoppingCart.getId())
                .orElseThrow(() -> new EntityNotFoundException("Can not find a cart item"));

        Set<CartItem> cartItems = shoppingCart.getCartItems();
        cartItems.remove(cartItem);
        cartItemsMapper.updateItemFromDto(requestDto, cartItem);
        cartItems.add(cartItem);
        cartItemRepository.save(cartItem);

        shoppingCart.setCartItems(cartItems);
        shoppingCartRepository.save(shoppingCart);

        return getResponseDtoWithNewItem(shoppingCart, cartItem, cartItems);
    }

    @Override
    public void deleteItem(Long id) {
        ShoppingCart shoppingCart = shoppingCartRepository.getShoppingCartByUserId(getUserId());
        CartItem cartItem = cartItemRepository.findByIdAndShoppingCartId(id, shoppingCart.getId())
                .orElseThrow(() -> new EntityNotFoundException("Can not find a cart item"));

        cartItemRepository.delete(cartItem);
    }

    protected void createShoppingCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCartRepository.save(shoppingCart);
    }

    private Long getUserId() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        return userRepository.getUserByEmail(username).getId();
    }

    private ShoppingCartDto getResponseDtoWithNewItem(ShoppingCart shoppingCart,
                                           CartItem cartItem, Set<CartItem> cartItems) {
        ShoppingCartDto shoppingCartDto = shoppingCartMapper.toDto(shoppingCart);
        Set<CartItemResponseDto> cartItemResponseDto = cartItemsMapper.toDtoSet(cartItems);
        cartItemResponseDto.add(cartItemsMapper.toResponseDto(cartItem));
        shoppingCartDto.setCartItems(cartItemResponseDto);

        return shoppingCartDto;
    }

    private ShoppingCartDto getResponseDto(ShoppingCart shoppingCart, Set<CartItem> cartItems) {
        ShoppingCartDto shoppingCartDto = shoppingCartMapper.toDto(shoppingCart);
        Set<CartItemResponseDto> cartItemResponseDto = cartItemsMapper.toDtoSet(cartItems);
        shoppingCartDto.setCartItems(cartItemResponseDto);

        return shoppingCartDto;
    }
}

