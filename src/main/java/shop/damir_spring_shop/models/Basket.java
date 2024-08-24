package shop.damir_spring_shop.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import shop.damir_spring_shop.models.enums.BasketStatus;

@Entity
@Table(name = "baskets")
@Getter
@Setter
public class Basket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "amount")
    private int amount;

    @Column(name = "status")
    private BasketStatus status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
}
