package mate.academy.application.mapper;

import mate.academy.application.config.MapperConfig;
import mate.academy.application.dto.cart.ShoppingCartDto;
import mate.academy.application.model.ShoppingCart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface ShoppingCartMapper {
    @Mapping(source = "user.id", target = "userId")

    ShoppingCartDto toDto(ShoppingCart shoppingCart);

    ShoppingCart toEntity(ShoppingCartDto shoppingCartDto);

}
