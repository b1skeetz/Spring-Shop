package shop.damir_spring_shop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.damir_spring_shop.models.PropValues;

import java.util.List;

public interface PropValuesRepository extends JpaRepository<PropValues, Long> {

}
