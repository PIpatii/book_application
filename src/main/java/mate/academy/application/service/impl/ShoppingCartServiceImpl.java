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

@Service
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
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.getUserByEmail(username);
        ShoppingCart shoppingCart = shoppingCartRepository.getShoppingCartByUser(user);

        return getResponseDto(shoppingCart, shoppingCart.getCartItems());
    }

    @Override
    public ShoppingCartDto saveItem(AddCartItemRequestDto requestDto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.getUserByEmail(username);
        ShoppingCart shoppingCart = shoppingCartRepository.getShoppingCartByUser(user);

        CartItem cartItem = cartItemsMapper.addCartRequestToModel(requestDto);
        cartItem.setBook(bookRepository.findBooksById(requestDto.getBookId()));
        cartItem.setShoppingCart(shoppingCart);
        cartItemRepository.save(cartItem);

        Set<CartItem> cartItems = shoppingCart.getCartItems();
        cartItems.add(cartItem);
        shoppingCart.setCartItems(cartItems);
        shoppingCartRepository.save(shoppingCart);

        return getResponseDtoWithNewItem(shoppingCart, cartItem, cartItems);
    }

    @Override
    public ShoppingCartDto updateItem(UpdateCartItemRequestDto requestDto, Long id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.getUserByEmail(username);
        ShoppingCart shoppingCart = shoppingCartRepository.getShoppingCartByUser(user);

        CartItem cartItem = cartItemRepository.findById(id).orElseThrow(()
                -> new EntityNotFoundException("CartItem with id " + id + " not found!"));

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
        cartItemRepository.deleteById(id);

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

