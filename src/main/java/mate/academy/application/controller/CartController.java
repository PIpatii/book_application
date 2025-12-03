package mate.academy.application.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mate.academy.application.dto.cart.ShoppingCartDto;
import mate.academy.application.dto.cart.item.AddCartItemRequestDto;
import mate.academy.application.dto.cart.item.UpdateCartItemRequestDto;
import mate.academy.application.service.ShoppingCartService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Shopping Cart", description = "Endpoints for Shopping Cart")
@RequiredArgsConstructor
@RestController
@RequestMapping("/carts")
public class CartController {
    private final ShoppingCartService shoppingCartService;

    @PreAuthorize("hasRole('USER')")
    @GetMapping
    @Operation(summary = "Get Shopping cart", description = "Get Shopping Cart current user")
    public ShoppingCartDto get() {
        return shoppingCartService.getShoppingCart();
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    @Operation(summary = "Add an item", description = "Add an item to a shopping cart")
    public ShoppingCartDto addItem(
            @RequestBody @Valid AddCartItemRequestDto addCartItemRequestDto
    ) {
        return shoppingCartService.saveItem(addCartItemRequestDto);
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/items/{cartItemId}")
    @Operation(summary = "update quantity", description = "update quantity of an item")
    public ShoppingCartDto updateQuantity(
            @RequestBody @Valid UpdateCartItemRequestDto updateCartItemRequestDto,
                @PathVariable Long cartItemId
    ) {
        return shoppingCartService.updateItem(updateCartItemRequestDto, cartItemId);
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/items/{cartItemId}")
    @Operation(summary = "delete an item", description = "delete an item using id")
    public void deleteItem(@PathVariable Long cartItemId) {
        shoppingCartService.deleteItem(cartItemId);
    }
}
