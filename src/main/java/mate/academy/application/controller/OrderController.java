package mate.academy.application.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.application.dto.order.OrderRequestDto;
import mate.academy.application.dto.order.OrderResponseDto;
import mate.academy.application.dto.order.StatusUpdateRequestDto;
import mate.academy.application.dto.order.item.OrderItemResponseDto;
import mate.academy.application.service.OrderItemService;
import mate.academy.application.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Order", description = "Endpoints for Order options")
@RequiredArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;
    private final OrderItemService orderItemService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    @Operation(summary = "Create an order", description = "Creating an order")
    @ResponseStatus(HttpStatus.CREATED)
    OrderResponseDto createOrder(@RequestBody @Valid OrderRequestDto orderRequestDto) {
        return orderService.createOrder(orderRequestDto);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping
    @Operation(summary = "Get Orders", description = "Get all orders current user")
    List<OrderResponseDto> getOrders() {
        return orderService.getOrders();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("{orderId}")
    @Operation(summary = "Update a status", description = "Updating a status")
    OrderResponseDto updateStatus(@RequestBody @Valid StatusUpdateRequestDto statusUpdateRequestDto,
                                  @PathVariable Long orderId) {
        return orderService.updateStatus(statusUpdateRequestDto, orderId);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("{orderId}/items")
    @Operation(summary = "Get all order items", description = "Get all order items by order id")
    List<OrderItemResponseDto> getAllOrderItemsByOrder(@PathVariable Long orderId) {
        return orderItemService.getAllByOrderId(orderId);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("{orderId}/items/{orderItemId}")
    @Operation(summary = "Get an order item by id", description = "Get order item by id")
    OrderItemResponseDto getOrderItemById(@PathVariable Long orderId,
                                          @PathVariable Long orderItemId) {
        return orderItemService.getOrderItemById(orderId, orderItemId);
    }

}
