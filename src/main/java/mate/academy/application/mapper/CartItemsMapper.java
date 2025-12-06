package mate.academy.application.mapper;

import java.util.Set;
import mate.academy.application.config.MapperConfig;
import mate.academy.application.dto.cart.item.AddCartItemRequestDto;
import mate.academy.application.dto.cart.item.CartItemResponseDto;
import mate.academy.application.dto.cart.item.UpdateCartItemRequestDto;
import mate.academy.application.model.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class, uses = BookMapper.class)
public interface CartItemsMapper {
    @Mapping(target = "book", source = "bookId", qualifiedByName = "bookFromId")
    @Mapping(target = "shoppingCart", ignore = true)
    CartItem addCartRequestToModel(AddCartItemRequestDto requestDto);

    @Mapping(source = "book.id", target = "bookId")
    @Mapping(source = "book.title", target = "bookTitle")
    CartItemResponseDto toResponseDto(CartItem cartItem);

    Set<CartItemResponseDto> toDtoSet(Set<CartItem> cartItems);

    void updateItemFromDto(UpdateCartItemRequestDto requestDto,
                           @MappingTarget CartItem cartItem);

}
