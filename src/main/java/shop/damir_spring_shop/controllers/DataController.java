package shop.damir_spring_shop.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import shop.damir_spring_shop.models.*;
import shop.damir_spring_shop.repositories.CategoryRepository;
import shop.damir_spring_shop.repositories.ProductRepository;
import shop.damir_spring_shop.repositories.PropValuesRepository;
import shop.damir_spring_shop.repositories.PropertyRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
@RequestMapping(path = "/products")
public class DataController {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final PropertyRepository propertyRepository;
    private final PropValuesRepository propValuesRepository;

    @GetMapping
    public String getProducts(Model model){
        model.addAttribute("products", productRepository.findAll());
        return "all_products";
    }

    @GetMapping(path = "/filter")
    public String filter(@RequestParam(name = "categoryName", required = false) String categoryName, Model model){
        List<Product> temp = productRepository.findProductsByCategory_NameContains(categoryName);
        if(temp.isEmpty()){
            return getProducts(model);
        }
        model.addAttribute("products", temp);
        model.addAttribute("category", categoryName);

        return "all_products";
    }

    @GetMapping(path = "/create")
    public String toCreatePage(@ModelAttribute(name = "newProduct") Product newProduct, Model model){
        model.addAttribute("allCategories", categoryRepository.findAll());
        model.addAttribute("propValues", new ArrayList<PropValues>());
        return "create_product";
    }

    @GetMapping(path = "/create/properties")
    public String toCreatePage(@RequestParam(name = "id") Long id, Model model){
        List<Property> properties = propertyRepository.findPropertiesByCategory_Id(id);
        model.addAttribute("properties", properties);

        return "properties";
    }

    @PostMapping // Выберите категорию для создания товара,
                    // а потом уже переход на страницу создания товара
    public String create(@ModelAttribute(name = "newProduct") Product newProduct,
                         @RequestParam(name="propValue") List<String> propValues){
        productRepository.save(newProduct);
        // Передать строковый массив, пройтись циклом по нему и создавать каждый раз новый propValue объект, передавая
        // в его поле value строку из массива.
        List<Property> props = propertyRepository.findAll().stream().filter(property ->
                Objects.equals(property.getCategory().getId(), newProduct.getCategory().getId())).toList();
        for (int i = 0; i < propValues.size(); i++) {
            PropValues temp = new PropValues();
            temp.setProduct(newProduct);
            temp.setValue(propValues.get(i));
            temp.setProperty(props.get(i));
            propValuesRepository.save(temp);
        }
        return "redirect:/products";
    }
}
