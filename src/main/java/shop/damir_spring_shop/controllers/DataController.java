package shop.damir_spring_shop.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import shop.damir_spring_shop.models.*;
import shop.damir_spring_shop.services.CategoryService;
import shop.damir_spring_shop.services.ProductService;
import shop.damir_spring_shop.services.PropValuesService;
import shop.damir_spring_shop.services.PropertyService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping(path = "/products")
public class DataController {
    private final ProductService productService;
    private final CategoryService categoryService;
    private final PropertyService propertyService;
    private final PropValuesService propValuesService;

    @GetMapping
    public String getProducts(Model model){
        model.addAttribute("products", productService.findAll());
        return "all_products";
    }

    @GetMapping(path = "/filter")
    public String filter(@RequestParam(name = "categoryName", required = false) String categoryName, Model model){
        List<Product> temp = productService.findProductsByCategoryNameContains(categoryName);
        if(temp.isEmpty()){
            return getProducts(model);
        }
        model.addAttribute("products", temp);
        model.addAttribute("category", categoryName);

        return "all_products";
    }

    @GetMapping(path = "/create")
    public String toCreatePage(@ModelAttribute(name = "newProduct") Product newProduct, Model model){
        model.addAttribute("allCategories", categoryService.findAll());
        model.addAttribute("propValues", new ArrayList<PropValues>());
        return "create_product";
    }

    @GetMapping(path = "/create/properties")
    public String toCreatePage(@RequestParam(name = "id") Long id, Model model){
        List<Property> properties = propertyService.getPropertiesByCategoryId(id);
        model.addAttribute("properties", properties);

        return "properties";
    }

    @PostMapping // Выберите категорию для создания товара,
                    // а потом уже переход на страницу создания товара
    public String create(@ModelAttribute(name = "newProduct") Product newProduct,
                         @ModelAttribute(name = "propValues") ArrayList<PropValues> propValues){
        productService.createProduct(newProduct);
        for (PropValues propValue : propValues) {
            propValuesService.createPropValues(propValue);
        }
        return "redirect:/products";
    }
}
