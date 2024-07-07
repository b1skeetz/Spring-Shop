package shop.damir_spring_shop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.damir_spring_shop.models.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
