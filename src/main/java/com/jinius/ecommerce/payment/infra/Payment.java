package com.jinius.ecommerce.payment.infra;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_payment")
@Getter
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long id;

    private Long orderId;

    private Long userId;

    private String type;

    private BigInteger amount;

    private BigInteger point;

    @CreatedDate
    private LocalDateTime createAt;
}
