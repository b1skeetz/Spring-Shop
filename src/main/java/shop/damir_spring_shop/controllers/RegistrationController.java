package shop.damir_spring_shop.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import shop.damir_spring_shop.models.User;
import shop.damir_spring_shop.models.enums.UserRole;
import shop.damir_spring_shop.repositories.UserRepository;

@Controller
@RequiredArgsConstructor
@RequestMapping(path = "/registration")
public class RegistrationController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping
    private String registrationPage(Model model){
        model.addAttribute("user", new User());
        return "auth/registration_page";
    }

    @PostMapping
    private String registration(@ModelAttribute(name = "user") User user){
        user.setRole(UserRole.USER);
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user.getLogin(), user.getPassword()
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return "redirect:/products";
    }
}