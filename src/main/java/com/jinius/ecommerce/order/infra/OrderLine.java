package com.jinius.ecommerce.order.infra;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigInteger;

@Entity
@Table(name = "tb_order_line")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
@Builder
public class OrderLine {
    /**
     * 주문 상품 ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_line_id")
    private Long id;

    /**
     * 주문 정보
     */
    private Long orderId;


    /**
     * 상품 정보
     */
    private Long itemId;

    /**
     * 상품 가격
     */
    @Column(nullable = false, columnDefinition = "BIGINT")
    private BigInteger itemPrice;

    /**
     * 주문 수량
     */
    @Column(nullable = false)
    private Long orderQuantity;

    /**
     * 상태
     */
    @Column(nullable = false)
    private String status;
}
