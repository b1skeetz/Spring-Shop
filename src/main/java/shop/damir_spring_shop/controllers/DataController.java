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
    public String getProducts(Model model) {
        model.addAttribute("products", productRepository.findAll());
        return "all_products";
    }

    @GetMapping(path = "/filter")
    public String filter(@RequestParam(name = "categoryName", required = false) String categoryName, Model model) {
        List<Product> temp = productRepository.findProductsByCategory_NameContains(categoryName);
        if (temp.isEmpty()) {
            return getProducts(model);
        }
        model.addAttribute("products", temp);
        model.addAttribute("category", categoryName);

        return "all_products";
    }

    @GetMapping(path = "/create")
    public String toCreatePage(@ModelAttribute(name = "newProduct") Product newProduct, Model model) {
        model.addAttribute("allCategories", categoryRepository.findAll());
        model.addAttribute("propValues", new ArrayList<PropValues>());
        return "create_product";
    }

    @GetMapping(path = "/create/properties")
    public String toCreatePage(@RequestParam(name = "id") Long id, Model model) {
        List<Property> properties = propertyRepository.findPropertiesByCategory_Id(id);
        model.addAttribute("properties", properties);

        return "properties";
    }

    @PostMapping // Выберите категорию для создания товара,
    // а потом уже переход на страницу создания товара
    public String create(@ModelAttribute(name = "newProduct") Product newProduct,
                         @RequestParam(name = "propValue") List<String> propValues) {
        // Передать строковый массив, пройтись циклом по нему и создавать каждый раз новый propValue объект, передавая
        // в его поле value строку из массива.
//        List<PropValues> filledPropValuesList = parsePropValues(newProduct, propValues);
//        newProduct.setPropValues(filledPropValuesList);

        productRepository.save(newProduct);

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

    @GetMapping(path = "/edit/{id}")
    public String toEditPage(@PathVariable(name = "id") Long id, Model model) {
        Product temp = productRepository.findById(id).orElse(new Product());
        model.addAttribute("product", temp); // добавить пустой список propValues

        return "edit_product";
    }

    @PostMapping("/edit/{id}")
    public String edit(@ModelAttribute("product") Product product,
                       @RequestParam(name = "propValue") List<String> propValues,
                       @PathVariable(name = "id") Long id){
        System.out.println(product); // propValues лист передается как null
        product.setId(id);
        productRepository.save(product);
        List<PropValues> productsPropValues = propValuesRepository.findAll()
                .stream().filter(pv -> Objects.equals(pv.getProduct().getId(), id)).toList();
        // `/edit?productId=...&productName=...&productPrice=...&optionId=2&value=AMD&optionId=3&value=ABC`
        // @RequestParam(name="optionId") List<Integer>
        // @RequestParam(name="value") List<String>
        if(productsPropValues.size() == propValues.size()){
            for (int i = 0; i < productsPropValues.size(); i++) {
                productsPropValues.get(i).setValue(propValues.get(i));
                propValuesRepository.save(productsPropValues.get(i));
            }
        } else {
            throw new RuntimeException(String.format("productsPropValues.size() {%d} is not equal " +
                    "with propValues.size() {%d}", productsPropValues.size(), propValues.size()));
        }
        return "redirect:/products";
    }
}
