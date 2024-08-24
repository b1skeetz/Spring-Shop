package shop.damir_spring_shop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.damir_spring_shop.models.Order;
import shop.damir_spring_shop.models.User;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findOrdersByUser(User user);
    List<Order> findAllByOrderByCreatedAtDesc();
}
