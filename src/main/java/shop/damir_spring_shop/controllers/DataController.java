package shop.damir_spring_shop.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import shop.damir_spring_shop.models.*;
import shop.damir_spring_shop.services.CategoryService;
import shop.damir_spring_shop.services.ProductService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping(path = "/products")
public class DataController {
    private final ProductService productService;
    private final CategoryService categoryService;

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
        return "create_product";
    }

    @PostMapping // Выберите категорию для создания товара,
                    // а потом уже переход на страницу создания товара
    public String create(@ModelAttribute(name = "newProduct") Product newProduct){
        productService.createProduct(newProduct);
        return "redirect:/products";
    }
}
