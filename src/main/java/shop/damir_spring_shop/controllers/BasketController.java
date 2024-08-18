package shop.damir_spring_shop.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import shop.damir_spring_shop.models.Basket;
import shop.damir_spring_shop.repositories.BasketRepository;
import shop.damir_spring_shop.repositories.ProductRepository;
import shop.damir_spring_shop.repositories.UserRepository;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping(path = "/basket")
public class BasketController {
    private final BasketRepository basketRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @GetMapping("{id}")
    public String showBasket(@PathVariable("id") Long id, Model model){
        List<Basket> baskets = basketRepository.findBasketsByUserId(id);
        model.addAttribute("baskets", baskets);

        return "basket";
    }

    @PostMapping("{id}/increase")
    public String increaseAmount(@PathVariable("id") Long id){
        //TODO Доделать метод увеличения количества товара
    }

    @PostMapping("{id}/decrease")
    public String increaseAmount(@PathVariable("id") Long id){
        //TODO Доделать метод уменьшения количества товара
    }

    @DeleteMapping("{id}")
    public String deleteBasket(@PathVariable("id") Long id){
        Basket basketForDelete = basketRepository.findBasketById(id);
        basketRepository.delete(basketForDelete);

        return "basket";
    }
}
