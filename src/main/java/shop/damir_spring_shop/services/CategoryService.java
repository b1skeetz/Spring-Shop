package shop.damir_spring_shop.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import shop.damir_spring_shop.models.Category;
import shop.damir_spring_shop.repositories.CategoryRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public List<Category> findAll(){
        return categoryRepository.findAll();
    }
}
