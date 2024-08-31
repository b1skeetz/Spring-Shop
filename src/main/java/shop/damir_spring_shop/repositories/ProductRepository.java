package shop.damir_spring_shop.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import shop.damir_spring_shop.models.Product;
import shop.damir_spring_shop.models.PropValues;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findProductsByNameContains(String name);
    Page<Product> findProductsByCategory_NameContains(String name, Pageable pageable);
    Product findProductsById(Long id);
}
