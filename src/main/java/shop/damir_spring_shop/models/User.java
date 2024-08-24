package shop.damir_spring_shop.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import shop.damir_spring_shop.models.enums.UserRole;

import javax.naming.Name;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "login")
    private String login;

    @Column(name = "password")
    private String password;

    @Column(name = "phone")
    private String phone;

    @Column(name="role")
    private UserRole role;

    @OneToMany(mappedBy = "user")
    private List<Feedback> feedbacks;

    @OneToMany(mappedBy = "user")
    private List<Order> orders;
}
