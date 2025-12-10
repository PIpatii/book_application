package mate.academy.application.mapper;

import mate.academy.application.config.MapperConfig;
import mate.academy.application.dto.order.OrderRequestDto;
import mate.academy.application.dto.order.OrderResponseDto;
import mate.academy.application.dto.order.StatusUpdateRequestDto;
import mate.academy.application.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface OrderMapper {
    Order toEntity(OrderRequestDto orderRequestDto);

    @Mapping(target = "userId", source = "user.id")
    OrderResponseDto toDto(Order order);

    void updateOrderStatus(StatusUpdateRequestDto statusUpdateRequestDto,
                           @MappingTarget Order order);
}
