package shop.damir_spring_shop.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import shop.damir_spring_shop.models.enums.OrderStatus;

import java.time.DateTimeException;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "number")
    private String number;

    @Column(name = "status")
    private OrderStatus status;

    @Column(name = "total_sum")
    private int totalSum;

    @Column(name = "created_at")
    private Date createdAt;

    @OneToMany(mappedBy = "order")
    private List<Basket> baskets;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
