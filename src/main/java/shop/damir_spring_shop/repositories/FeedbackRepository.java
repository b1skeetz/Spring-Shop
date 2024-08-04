package shop.damir_spring_shop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.damir_spring_shop.models.Feedback;
import shop.damir_spring_shop.models.Product;
import shop.damir_spring_shop.models.User;

import java.util.Optional;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    Optional<Feedback> findFeedbackByUserAndProduct(User user, Product product);
}
