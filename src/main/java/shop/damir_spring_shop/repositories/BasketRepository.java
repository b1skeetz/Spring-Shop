package shop.damir_spring_shop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.damir_spring_shop.models.Basket;
import shop.damir_spring_shop.models.Product;

import java.util.List;
import java.util.Optional;

public interface BasketRepository extends JpaRepository<Basket, Long> {
    List<Basket> findBasketsByUserId(Long id);
    Basket findBasketById(Long id);

    boolean existsByUserIdAndProductId(Long userId, Long productId);
}
