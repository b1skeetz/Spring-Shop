package shop.damir_spring_shop.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import shop.damir_spring_shop.models.User;
import shop.damir_spring_shop.repositories.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getCurrentUser(){
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)) {
            String login = authentication.getName();
            return userRepository.findByLogin(login).orElseThrow(() -> {
                String message = "User is not found by login: `%s`".formatted(login);
                return new RuntimeException(message);
            });
        } else {
            return null;
        }
    }
}