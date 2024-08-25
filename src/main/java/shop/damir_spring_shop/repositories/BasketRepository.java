package shop.damir_spring_shop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.damir_spring_shop.models.Basket;
import shop.damir_spring_shop.models.Product;
import shop.damir_spring_shop.models.enums.BasketStatus;
import shop.damir_spring_shop.models.enums.OrderStatus;

import java.util.List;
import java.util.Optional;

public interface BasketRepository extends JpaRepository<Basket, Long> {
    List<Basket> findBasketsByUserId(Long id);
    List<Basket> findBasketsByUserIdAndStatus(Long id, BasketStatus status);
    List<Basket> findBasketsByUserIdAndStatusOrderById(Long id, BasketStatus status);
    Basket findBasketById(Long id);

    boolean existsByUserIdAndProductIdAndStatus(Long userId, Long productId, BasketStatus status);
}
