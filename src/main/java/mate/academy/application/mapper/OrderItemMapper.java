package mate.academy.application.mapper;

import java.util.Set;
import mate.academy.application.config.MapperConfig;
import mate.academy.application.dto.order.item.OrderItemResponseDto;
import mate.academy.application.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface OrderItemMapper {
    @Mapping(target = "bookId", source = "book.id")
    OrderItemResponseDto toDto(OrderItem orderItem);

    Set<OrderItemResponseDto> toDtoSet(Set<OrderItem> orderItems);
}
