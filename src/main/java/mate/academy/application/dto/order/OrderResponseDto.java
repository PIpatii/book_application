package mate.academy.application.dto.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import mate.academy.application.dto.order.item.OrderItemResponseDto;

@Getter
@Setter
public class OrderResponseDto {
    private Long id;
    private Long userId;
    private Set<OrderItemResponseDto> items;
    private LocalDateTime orderDate;
    private BigDecimal total;
    private String status;
}
