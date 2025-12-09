package mate.academy.application.service.impl;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.application.dto.order.item.OrderItemResponseDto;
import mate.academy.application.mapper.OrderItemMapper;
import mate.academy.application.model.Order;
import mate.academy.application.model.OrderItem;
import mate.academy.application.repository.order.OrderRepository;
import mate.academy.application.service.OrderItemService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OrderItemServiceImpl implements OrderItemService {
    private final OrderRepository orderRepository;
    private final OrderItemMapper orderItemMapper;

    @Override
    public OrderItemResponseDto getOrderItemById(Long orderId, Long orderItemId) {
        Order order = orderRepository.getOrderById(orderId);
        OrderItem orderItem = order.getOrderItems().stream()
                .filter(item -> item.getId().equals(orderItemId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Order Item with id "
                        + orderItemId + " not found"));

        return orderItemMapper.toDto(orderItem);
    }

    @Override
    public List<OrderItemResponseDto> getAllByOrderId(Long orderId) {
        Order order = orderRepository.getOrderById(orderId);

        return order.getOrderItems().stream()
                .map(orderItemMapper::toDto)
                .toList();
    }
}
