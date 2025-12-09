package mate.academy.application.repository.order;

import java.util.List;
import mate.academy.application.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> getAllByUserId(Long userId);

    Order getOrderById(Long orderId);
}
