package shop.damir_spring_shop.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import shop.damir_spring_shop.models.Order;
import shop.damir_spring_shop.models.enums.OrderStatus;
import shop.damir_spring_shop.repositories.OrderRepository;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping(path = "/admin-orders")
public class AdminOrderController {
    private final OrderRepository orderRepository;

    @GetMapping()
    public String getOrders(Model model,
                            @ModelAttribute("orderStatus") Order orderStatus){
        List<Order> orders = orderRepository.findAllByOrderByCreatedAtDesc();
        model.addAttribute("orders", orders);

        return "admin/orders";
    }

    @PostMapping("/{id}/status")
    public String changeStatus(@PathVariable("id") Long id,
                               @ModelAttribute("orderStatus") Order orderStatus){
        Order orderFromDb = orderRepository.findById(id).orElse(null);

        if(orderFromDb != null){
            orderFromDb.setStatus(orderStatus.getStatus());
            orderRepository.save(orderFromDb);
        }

        return "redirect:/admin-orders";
    }
}
