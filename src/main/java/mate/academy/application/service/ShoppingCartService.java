package mate.academy.application.service;

import mate.academy.application.dto.cart.ShoppingCartDto;
import mate.academy.application.dto.cart.item.AddCartItemRequestDto;
import mate.academy.application.dto.cart.item.UpdateCartItemRequestDto;

public interface ShoppingCartService {
    ShoppingCartDto getShoppingCart();

    ShoppingCartDto saveItem(AddCartItemRequestDto requestDto);

    ShoppingCartDto updateItem(UpdateCartItemRequestDto requestDto, Long id);

    void deleteItem(Long id);
}
