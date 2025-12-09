package mate.academy.application.service.impl;

import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import mate.academy.application.dto.order.OrderRequestDto;
import mate.academy.application.dto.order.OrderResponseDto;
import mate.academy.application.dto.order.StatusUpdateRequestDto;
import mate.academy.application.mapper.OrderItemMapper;
import mate.academy.application.mapper.OrderMapper;
import mate.academy.application.model.CartItem;
import mate.academy.application.model.Order;
import mate.academy.application.model.OrderItem;
import mate.academy.application.model.ShoppingCart;
import mate.academy.application.model.User;
import mate.academy.application.repository.cart.ShoppingCartRepository;
import mate.academy.application.repository.order.OrderItemRepository;
import mate.academy.application.repository.order.OrderRepository;
import mate.academy.application.repository.user.UserRepository;
import mate.academy.application.service.OrderService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class OrderServiceImpl implements OrderService {
    private final UserRepository userRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderItemMapper orderItemMapper;
    private final OrderMapper orderMapper;

    @Override
    public OrderResponseDto createOrder(OrderRequestDto orderRequestDto) {
        Order order = orderMapper.toEntity(orderRequestDto);
        order.setUser(getAuthenticatedUser());
        order.setOrderDate(LocalDateTime.now());
        order.setOrderItems(getUserOrderItems(order));
        order.setTotal(getTotalPrice(order.getOrderItems()));
        order.setStatus(Order.Status.COMPLETED);

        OrderResponseDto responseDto = orderMapper.toDto(orderRepository.save(order));
        orderItemRepository.saveAll(order.getOrderItems());

        return getResponse(order);
    }

    @Override
    public List<OrderResponseDto> getOrders() {
        return orderRepository.getAllByUserId(getAuthenticatedUser().getId()).stream()
                .map(this::getResponse)
                .toList();
    }

    @Override
    public OrderResponseDto updateStatus(StatusUpdateRequestDto statusUpdateRequestDto,
                                         Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Can not find order with id: " + orderId));
        orderMapper.updateOrderStatus(statusUpdateRequestDto, order);

        return getResponse(orderRepository.save(order));
    }

    private OrderResponseDto getResponse(Order order) {
        OrderResponseDto responseDto = orderMapper.toDto(order);
        responseDto.setItems(orderItemMapper.toDtoSet(order.getOrderItems()));

        return responseDto;
    }

    private BigDecimal getTotalPrice(Set<OrderItem> orderItems) {
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (OrderItem orderItem : orderItems) {
            totalPrice = totalPrice.add(orderItem.getPrice());
        }
        return totalPrice;
    }

    private Set<OrderItem> getUserOrderItems(Order order) {
        ShoppingCart shoppingCart = shoppingCartRepository.getShoppingCartByUserId(
                getAuthenticatedUser().getId());
        Set<OrderItem> orderItems = new HashSet<>();

        for (CartItem cartItem : shoppingCart.getCartItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setBook(cartItem.getBook());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getBook().getPrice());
            orderItems.add(orderItem);
        }
        return orderItems;
    }

    private User getAuthenticatedUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        return userRepository.getUserByEmail(username);
    }
}
