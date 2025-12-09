package mate.academy.application.service;

import java.util.List;
import mate.academy.application.dto.order.item.OrderItemResponseDto;

public interface OrderItemService {
    OrderItemResponseDto getOrderItemById(Long orderId, Long orderItemId);

    List<OrderItemResponseDto> getAllByOrderId(Long orderId);
}
