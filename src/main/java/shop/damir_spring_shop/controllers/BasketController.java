package shop.damir_spring_shop.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import shop.damir_spring_shop.models.Basket;
import shop.damir_spring_shop.models.User;
import shop.damir_spring_shop.repositories.BasketRepository;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping(path = "/basket")
public class BasketController {
    private final BasketRepository basketRepository;

    // TODO Внедрить логику с айдишником пользователя, когда появится авторизация

    @GetMapping("{id}")
    public String showBasket(@PathVariable("id") Long id, Model model) {
        List<Basket> baskets = basketRepository.findBasketsByUserId(id);
        model.addAttribute("baskets", baskets);

        return "basket";
    }

    @PostMapping("{id}/increase")
    public String increaseAmount(@PathVariable("id") Long id, Model model) {
        Basket basket = basketRepository.findBasketById(id);
        basket.setAmount(basket.getAmount() + 1);
        basketRepository.save(basket);

        return showBasket(basket.getUser().getId(), model);
    }

    @PostMapping("{id}/decrease")
    public String decreaseAmount(@PathVariable("id") Long id, Model model) {
        Basket basket = basketRepository.findBasketById(id);
        basket.setAmount(basket.getAmount() - 1);
        if (basket.getAmount() == 0) {
            User user = basket.getUser();
            deleteBasket(id, model);
            return showBasket(user.getId(), model);
        }
        basketRepository.save(basket);

        return showBasket(basket.getUser().getId(), model);
    }

    @PostMapping("{id}")
    public String deleteBasket(@PathVariable("id") Long id, Model model) {
        Basket basketForDelete = basketRepository.findBasketById(id);
        User user = basketForDelete.getUser();
        basketRepository.delete(basketForDelete);

        return showBasket(user.getId(), model);
    }
}
