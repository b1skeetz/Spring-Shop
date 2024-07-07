package shop.damir_spring_shop.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import shop.damir_spring_shop.models.Product;
import shop.damir_spring_shop.repositories.ProductRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public List<Product> findAll(){
        return productRepository.findAll();
    }

    public List<Product> findProductsByCategoryNameContains(String name){
        return productRepository.findProductsByCategory_NameContains(name);
    }

    public void createProduct(Product product){
        productRepository.save(product);
    }
}
