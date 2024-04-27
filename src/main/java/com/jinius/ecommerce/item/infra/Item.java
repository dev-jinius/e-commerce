package com.jinius.ecommerce.item.infra;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigInteger;

@Entity
@Table(name = "tb_item")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;

    @Column(name = "item_name", nullable = false, length = 50)
    private String name;

    @Column(name = "item_price", nullable = false, columnDefinition = "BIGINT")
    private BigInteger price;

    @Column(name = "stock_quantity", nullable = false)
    private Long quantity;
}
