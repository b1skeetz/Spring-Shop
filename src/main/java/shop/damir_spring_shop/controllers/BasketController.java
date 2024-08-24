package shop.damir_spring_shop.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import shop.damir_spring_shop.models.Basket;
import shop.damir_spring_shop.models.User;
import shop.damir_spring_shop.models.enums.BasketStatus;
import shop.damir_spring_shop.repositories.BasketRepository;
import shop.damir_spring_shop.services.UserService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping(path = "/basket")
public class BasketController {
    private final BasketRepository basketRepository;
    private final UserService userService;

    @GetMapping()
    public String showBasket(Model model) {
        User currentUser = userService.getCurrentUser();
        List<Basket> baskets = basketRepository.findBasketsByUserIdAndStatus(currentUser.getId(), BasketStatus.PENDING);
        int sum = 0;

        for (Basket basket : baskets) {
            sum += basket.getProduct().getPrice() * basket.getAmount();
        }
        model.addAttribute("baskets", baskets);
        model.addAttribute("totalSum", sum);
        model.addAttribute("isBasketEmpty", baskets.isEmpty());

        return "basket";
    }

    @PostMapping("{id}/increase")
    public String increaseAmount(@PathVariable("id") Long id, Model model) {
        Basket basket = basketRepository.findBasketById(id);
        basket.setAmount(basket.getAmount() + 1);
        basketRepository.save(basket);

        return showBasket(model);
    }

    @PostMapping("{id}/decrease")
    public String decreaseAmount(@PathVariable("id") Long id, Model model) {
        Basket basket = basketRepository.findBasketById(id);
        basket.setAmount(basket.getAmount() - 1);
        if (basket.getAmount() == 0) {
            deleteBasket(id, model);
            return showBasket(model);
        }
        basketRepository.save(basket);

        return showBasket(model);
    }

    @PostMapping("{id}")
    public String deleteBasket(@PathVariable("id") Long id, Model model) {
        Basket basketForDelete = basketRepository.findBasketById(id);
        basketRepository.delete(basketForDelete);

        return showBasket(model);
    }
}
