package shop.damir_spring_shop.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import shop.damir_spring_shop.models.*;
import shop.damir_spring_shop.models.enums.BasketStatus;
import shop.damir_spring_shop.repositories.*;
import shop.damir_spring_shop.services.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping(path = "/products")
public class ProductController {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final PropertyRepository propertyRepository;
    private final PropValuesRepository propValuesRepository;
    private final UserRepository userRepository;
    private final FeedbackRepository feedbackRepository;
    private final BasketRepository basketRepository;
    private final UserService userService;

    @GetMapping
    public String getProducts(Model model) {
        List<Product> products = productRepository.findAll();

        model.addAttribute("products", products);
        return "all_products";
    }

    @GetMapping(path = "/filter")
    public String filter(@RequestParam(name = "categoryName", required = false) String categoryName, Model model) {
        List<Product> temp = productRepository.findProductsByCategory_NameContains(categoryName);
        if (temp.isEmpty()) {
            return "redirect:/products";
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

    @PostMapping
    public String create(@ModelAttribute(name = "newProduct") Product newProduct,
                         @RequestParam(name = "propValue") List<String> propValues) {
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

    @GetMapping(path = "/{id}/feedbacks")
    public String getProductFeedbacks(@PathVariable(name = "id") Long id, Model model) {
        List<Feedback> feedbacks = feedbackRepository.findFeedbackByProduct_IdAndReleaseStatus(id, false);
        model.addAttribute("feedbacks", feedbacks);

        return "admin/feedbacks";
    }

    @PostMapping("/edit/{id}")
    public String edit(@ModelAttribute("product") Product product,
                       @RequestParam(name = "propValue") List<String> propValues,
                       @PathVariable(name = "id") Long id) {
        System.out.println(product); // propValues лист передается как null
        product.setId(id);
        productRepository.save(product);
        List<PropValues> productsPropValues = propValuesRepository.findAll()
                .stream().filter(pv -> Objects.equals(pv.getProduct().getId(), id)).toList();
        if (productsPropValues.size() == propValues.size()) {
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

    @GetMapping("{id}")
    public String getOneProduct(@PathVariable("id") Long id, Model model,
                                @ModelAttribute("feedback") Feedback feedback) {
        Product product = productRepository.findProductsById(id);
        List<Feedback> approvedFeedbacks = feedbackRepository.findFeedbackByProduct_IdAndReleaseStatus(id, true);
        model.addAttribute("product", product);
        model.addAttribute("feedbacks", approvedFeedbacks);

        double avgRate;
        double sum = 0d;
        for (Feedback productFeedback : approvedFeedbacks) {
            sum += productFeedback.getMark();
        }
        if (approvedFeedbacks.size() != 0) {
            avgRate = sum / approvedFeedbacks.size();
        } else {
            avgRate = 0d;
        }
        model.addAttribute("averageRate", avgRate);

        User currentUser = userService.getCurrentUser();
        if (currentUser != null) {
            boolean ifBasketExist = basketRepository.existsByUserIdAndProductIdAndStatus(currentUser.getId(), product.getId(), BasketStatus.PENDING);
            boolean ifFeedbackExists = feedbackRepository.findFeedbackByUserAndProduct(currentUser, product).isPresent();
            model.addAttribute("ifBasketExist", ifBasketExist);
            model.addAttribute("ifFeedbackExists", ifFeedbackExists);
        }

        return "one_product";
    }

    @PostMapping("{id}")
    public String saveFeedback(@PathVariable("id") Long id, Model model,
                               @ModelAttribute(name = "feedback") Feedback feedback) {

        User currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            return "redirect:/login";
        }
        Product currentProduct = productRepository.findProductsById(id);
        model.addAttribute("product", currentProduct);
        Optional<Feedback> ifFeedbackExists = feedbackRepository.findFeedbackByUserAndProduct(currentUser, currentProduct);
        if (ifFeedbackExists.isPresent()) {
            System.out.println("Вы уже отправляли отзыв!");
            return "redirect:/products/{id}";
        }
        feedback.setReleaseStatus(false);
        feedback.setUser(currentUser);
        feedback.setProduct(currentProduct);

        feedbackRepository.save(feedback);

        return "redirect:/products/{id}";
    }

    @PostMapping("{id}/basket")
    public String addProductToBasket(@PathVariable("id") Long id) {
        User currentUser = userService.getCurrentUser();
        Product currentProduct = productRepository.findProductsById(id);
        int amount = 1;

        Basket basket = new Basket();
        basket.setUser(currentUser);
        basket.setProduct(currentProduct);
        basket.setAmount(amount);
        basket.setStatus(BasketStatus.PENDING);

        basketRepository.save(basket);

        return "redirect:/products";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") Long id) {
        productRepository.deleteById(id);
        return "redirect:/products";
    }
}
