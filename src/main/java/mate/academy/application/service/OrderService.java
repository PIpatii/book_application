package mate.academy.application.service;

import java.util.List;
import mate.academy.application.dto.order.OrderRequestDto;
import mate.academy.application.dto.order.OrderResponseDto;
import mate.academy.application.dto.order.StatusUpdateRequestDto;

public interface OrderService {
    OrderResponseDto createOrder(OrderRequestDto orderRequestDto);

    List<OrderResponseDto> getOrders();

    OrderResponseDto updateStatus(StatusUpdateRequestDto statusUpdateRequestDto, Long orderId);

}
