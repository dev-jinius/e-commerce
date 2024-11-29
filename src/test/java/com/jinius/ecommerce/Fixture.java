package com.jinius.ecommerce;

import com.jinius.ecommerce.order.domain.model.*;
import com.jinius.ecommerce.payment.domain.model.*;
import com.jinius.ecommerce.product.domain.model.Product;
import com.jinius.ecommerce.product.domain.model.Stock;
import com.jinius.ecommerce.user.domain.model.User;
import com.navercorp.fixturemonkey.ArbitraryBuilder;
import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.FieldReflectionArbitraryIntrospector;

import java.math.BigInteger;
import java.util.List;

public class Fixture {
    public static final FixtureMonkey fixtureMonkey = FixtureMonkey.builder()
            .objectIntrospector(FieldReflectionArbitraryIntrospector.INSTANCE)
            .build();

    private static ArbitraryBuilder<User> fixUserName() {
        return fixtureMonkey.giveMeBuilder(User.class)
                .set("name", "이리스")
                .set("version", 1);
    }
    private static ArbitraryBuilder<Payment> fixPointPayment() {
        return fixtureMonkey.giveMeBuilder(Payment.class)
                .set("type", "POINT")
                .set("amount", BigInteger.ZERO);
    }

    /**
     * 주문 관련 Fixture
     */
    public static OrderSheet randomOrderSheet() {
        return fixtureMonkey.giveMeOne(OrderSheet.class);
    }

    public static OrderSheet customOrderSheet(Long userId, List<OrderItem> items) {
        return fixtureMonkey.giveMeBuilder(OrderSheet.class)
                .set("userId", userId)
                .set("orderItems", items)
                .sample();
    }

    public static OrderSheet orderSheet(Long userId) {
        List<OrderItem> orderItems = List.of(
                new OrderItem(1L, BigInteger.valueOf(49900), 2L)
        );

        return fixtureMonkey.giveMeBuilder(OrderSheet.class)
                .set("userId", userId)
                .set("orderItems", orderItems)
                .set("totalPrice", orderItems.stream().map(OrderItem::calculateTotalPrice).reduce(BigInteger.ZERO, BigInteger::add))
                .set("orderStatus", OrderStatus.PENDING)
                .sample();
    }

    public static Order order(Long userId) {
        List<OrderItem> orderItems = List.of(
                new OrderItem(1L, BigInteger.valueOf(49900), 2L)
        );
        return fixtureMonkey.giveMeBuilder(Order.class)
                .set("orderId", 1L)
                .set("userId", userId)
                .set("orderItems", orderItems)
                .set("totalPrice", orderItems.stream().map(OrderItem::calculateTotalPrice).reduce(BigInteger.ZERO, BigInteger::add))
                .set("orderStatus", OrderStatus.PENDING)
                .sample();
    }

    public static List<OrderItem> orderItems(int size) {
        return fixtureMonkey.giveMe(OrderItem.class, size);
    }

    public static List<OrderItem> orderItem() {
        return List.of(
                new OrderItem(1L, BigInteger.valueOf(49900), 1L)
        );
    }

    public static List<OrderItem> orderItems() {
        return List.of(
                new OrderItem(1L, BigInteger.valueOf(49900), 2L),
                new OrderItem(2L, BigInteger.valueOf(15000), 3L)
        );
    }

    /**
     * 유저 관련 Fixture
     */
    public static User negativeUser() {
        return fixUserName()
                .set("userId", -1L)
                .set("point", BigInteger.ZERO)
                .sample();
    }

    public static User user(Long id, int point) {
        return fixUserName()
                .set("userId", id)
                .set("point", BigInteger.valueOf(point))
                .sample();
    }

    /**
     * 결제 관련 Fixture
     */
    public static OrderPayment orderPayment(User user, Order order, PaymentType paymentType) {
        return fixtureMonkey.giveMeBuilder(OrderPayment.class)
                .set("orderId", order.getOrderId())
                .set("userId", user.getUserId())
                .set("userPoint", user.getPoint())
                .set("type", paymentType)
                .set("orderPrice", order.getTotalPrice())
                .sample();
    }

    public static OrderPaymentInfo orderPaymentInfoForPoint(User user, Order order, PaymentType paymentType) {
        return fixtureMonkey.giveMeBuilder(OrderPaymentInfo.class)
                .set("orderId", order.getOrderId())
                .set("userId", user.getUserId())
                .set("type", paymentType)
                .set("amount", BigInteger.ZERO)
                .set("point", order.getTotalPrice())
                .set("status", PaymentStatus.PNEDING)
                .sample();
    }

    public static Payment payment(OrderPaymentInfo orderPaymentInfo) {
        return fixtureMonkey.giveMeBuilder(Payment.class)
                .set("orderId", orderPaymentInfo.getOrderId())
                .set("userId", orderPaymentInfo.getUserId())
                .set("type", orderPaymentInfo.getType())
                .set("amount", orderPaymentInfo.getAmount())
                .set("point", orderPaymentInfo.getPoint())
                .set("status", PaymentStatus.PAID)
                .sample();
    }

    /**
     * 상품 관련 Fixture
     * @return
     */

    public static Stock stockOne() {
        return new Stock(1L, 50L);
    }

    public static List<Stock> stockList() {
        List<Stock> stocks = List.of(
                new Stock(1L, 50L),
                new Stock(3L, 50L)
        );
        return stocks;
    }

    public static List<Product> top5ProductList() {
        List<Product> products = List.of(
                new Product(1L, "청바지", BigInteger.valueOf(49900), 50L),
                new Product(5L, "아노락", BigInteger.valueOf(60000), 40L),
                new Product(4L, "바람막이", BigInteger.valueOf(100000), 30L),
                new Product(8L, "슬리퍼", BigInteger.valueOf(20000), 20L),
                new Product(2L, "원피스", BigInteger.valueOf(70000), 15L)
        );
        return products;
    }

    public static List<Product> ExceedProductsList() {
        List<Product> products = List.of(
                new Product(1L, "청바지", BigInteger.valueOf(49900), 50L),
                new Product(5L, "아노락", BigInteger.valueOf(60000), 40L),
                new Product(4L, "바람막이", BigInteger.valueOf(100000), 30L),
                new Product(8L, "슬리퍼", BigInteger.valueOf(20000), 20L),
                new Product(2L, "원피스", BigInteger.valueOf(70000), 15L),
                new Product(10L, "가디건", BigInteger.valueOf(80000), 10L)
        );
        return products;
    }
}