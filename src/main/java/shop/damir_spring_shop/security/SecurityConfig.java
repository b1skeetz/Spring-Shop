package shop.damir_spring_shop.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain (HttpSecurity httpSecurity) throws Exception{
        httpSecurity.formLogin(formLoginConfigurer -> {
            formLoginConfigurer.defaultSuccessUrl("/products");
        });
        httpSecurity.logout(logoutConfigurer -> {
            logoutConfigurer.logoutSuccessUrl("/products");
        });
        httpSecurity.authorizeHttpRequests(authorizationConfigurer -> {
            authorizationConfigurer
                    .requestMatchers(
                            "/products/create",
                            "/products/edit/{id}",
                            "/products/delete/{id}",
                            "/products/{id}/feedbacks"
                    ).hasRole("admin");
            authorizationConfigurer
                    .anyRequest()
                    .permitAll();
        });
        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}