package shop.damir_spring_shop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.damir_spring_shop.models.Basket;

import java.util.List;

public interface BasketRepository extends JpaRepository<Basket, Long> {
    List<Basket> findBasketsByUserId(Long id);
    Basket findBasketById(Long id);
}
