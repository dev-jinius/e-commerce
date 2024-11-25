package com.jinius.ecommerce.product.infra.entity;

import com.jinius.ecommerce.product.domain.model.Product;
import com.jinius.ecommerce.product.domain.model.Stock;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigInteger;

@Entity
@Table(name = "tb_product")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @Column(name = "product_name", nullable = false, length = 50)
    private String name;

    @Column(name = "product_price", nullable = false, columnDefinition = "BIGINT")
    private BigInteger price;

    @Column(name = "stock_quantity", nullable = false)
    private Long quantity;

    public Stock toStock() {
        return Stock.builder()
                .productId(getId())
                .quantity(getQuantity())
                .build();
    }

    public static ProductEntity fromStock(Stock stock) {
        return ProductEntity.builder()
                .id(stock.getProductId())
                .quantity(stock.getQuantity())
                .build();
    }

    public Product toProduct() {
        return Product.builder()
                .productId(getId())
                .productName(getName())
                .productPrice(getPrice())
                .quantity(getQuantity())
                .build();
    }

}
