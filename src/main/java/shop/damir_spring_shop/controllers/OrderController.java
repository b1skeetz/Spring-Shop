package shop.damir_spring_shop.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import shop.damir_spring_shop.models.Basket;
import shop.damir_spring_shop.models.Order;
import shop.damir_spring_shop.models.User;
import shop.damir_spring_shop.models.enums.BasketStatus;
import shop.damir_spring_shop.models.enums.OrderStatus;
import shop.damir_spring_shop.repositories.BasketRepository;
import shop.damir_spring_shop.repositories.OrderRepository;
import shop.damir_spring_shop.services.UserService;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping(path = "/orders")
public class OrderController {
    private final UserService userService;
    private final BasketRepository basketRepository;
    private final OrderRepository orderRepository;

    @GetMapping()
    public String getOrders(Model model){
        User user = userService.getCurrentUser();
        List<Order> orders = orderRepository.findOrdersByUser(user);

        model.addAttribute("orders", orders);
        return "orders";
    }

    @PostMapping("/create")
    public String createOrder(){
        User currentUser = userService.getCurrentUser();
        Order order = new Order();
        List<Basket> baskets = basketRepository.findBasketsByUserIdAndStatus(currentUser.getId(), BasketStatus.PENDING);
        order.setUser(currentUser);
        order.setNumber(UUID.randomUUID().toString());
        order.setStatus(OrderStatus.PENDING);
        order.setBaskets(baskets);
        order.setCreatedAt(new Date());
        int sum = 0;

        for (Basket basket : baskets) {
            sum += basket.getProduct().getPrice() * basket.getAmount();
        }
        order.setTotalSum(sum);
        orderRepository.save(order);
        for (Basket basket : baskets) {
            basket.setStatus(BasketStatus.PLACED);
            basket.setOrder(order);
            basketRepository.save(basket);
        }

        return "redirect:/products";
    }

    @PostMapping("/{id}/cancel")
    public String cancelOrder(@PathVariable("id") Long id){
        Order order = orderRepository.findById(id).orElse(null);
        if(order != null){
            order.setStatus(OrderStatus.CANCELED);
            orderRepository.save(order);
        }
        return "redirect:/orders";
    }
}
