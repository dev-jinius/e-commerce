package com.jinius.ecommerce;

import com.jinius.ecommerce.order.api.OrderItemRequest;
import com.jinius.ecommerce.order.api.OrderRequest;
import com.jinius.ecommerce.order.domain.*;
import com.jinius.ecommerce.payment.domain.Payment;
import com.jinius.ecommerce.product.domain.Stock;
import com.jinius.ecommerce.user.api.UserChargeRequest;
import com.jinius.ecommerce.user.domain.User;
import com.navercorp.fixturemonkey.ArbitraryBuilder;
import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.FieldReflectionArbitraryIntrospector;
import com.navercorp.fixturemonkey.api.type.TypeReference;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

public class Fixture {

    public static final FixtureMonkey fixtureMonkey = FixtureMonkey.builder()
            .objectIntrospector(FieldReflectionArbitraryIntrospector.INSTANCE)
            .build();

    private static ArbitraryBuilder<User> fixUserName() {
        return fixtureMonkey.giveMeBuilder(User.class)
                .set("name", "이리스");
    }

    private static ArbitraryBuilder<Order> fixOrderId() {
        return fixtureMonkey.giveMeBuilder(Order.class)
                .set("orderId", 1L);
    }

    private static ArbitraryBuilder<Payment> fixPointPayment() {
        return fixtureMonkey.giveMeBuilder(Payment.class)
                .set("type", "POINT")
                .set("amount", BigInteger.ZERO);
    }

    private static ArbitraryBuilder<List<Stock>> fixStockList() {
        return fixtureMonkey.giveMeBuilder(new TypeReference<List<Stock>>() {});
    }

    public static User notExistUser() {
        return fixUserName()
                .set("userId", 999L)
                .set("point", BigInteger.ZERO)
                .sample();
    }

    public static User negativeIdUser() {
        return fixUserName()
                .set("userId", -1L)
                .set("point", BigInteger.ZERO)
                .sample();
    }

    public static User existUser() {
        return fixUserName()
                .set("userId", 1L)
                .set("point", BigInteger.valueOf(50000))
                .sample();
    }

    public static User chargedExistUser(BigInteger point) {
        return fixUserName()
                .set("userId", 1L)
                .set("point", BigInteger.valueOf(50000).add(point))
                .sample();
    }

    public static User id1With50000Point() {
        return fixUserName()
                .set("userId", 1L)
                .set("point", BigInteger.valueOf(50000))
                .sample();
    }

    public static User id100With50000Point() {
        return fixUserName()
                .set("userId", 100L)
                .set("point", BigInteger.valueOf(50000))
                .sample();
    }

    public static UserChargeRequest chargeRequestId1WithNegativePoint() {
        return fixtureMonkey.giveMeBuilder(UserChargeRequest.class)
                .set("userId", 1L)
                .set("chargePoint", BigInteger.valueOf(-30000))
                .sample();
    }

    public static UserChargeRequest chargeRequestId1() {
        return fixtureMonkey.giveMeBuilder(UserChargeRequest.class)
                .set("userId", 1L)
                .set("chargePoint", BigInteger.valueOf(30000))
                .sample();
    }

    public static OrderRequest orderRequestRandomOne() {

        return fixtureMonkey.giveMeOne(OrderRequest.class);
    }

    public static OrderRequest orderRequestId1With49900PriceProduct() {
        List<OrderItemRequest> itemList = List.of(
                new OrderItemRequest(1L, BigInteger.valueOf(49900), 1L)
        );

        return fixtureMonkey.giveMeBuilder(OrderRequest.class)
                .set("userId", 1L)
                .set("orderItems", itemList)
                .sample();
    }

    public static OrderRequest orderRequestId100WithOneProduct() {
        List<OrderItemRequest> orderItems = fixtureMonkey.giveMe(OrderItemRequest.class, 1);

        List<OrderItemRequest> itemList = List.of(
                new OrderItemRequest(1L, BigInteger.valueOf(49900), 1L)
        );

        return fixtureMonkey.giveMeBuilder(OrderRequest.class)
                .set("userId", 100L)
                .set("orderItems", itemList)
                .sample();
    }

    public static OrderRequest orderRequestId1WithOneProduct() {
        List<OrderItemRequest> orderItems = fixtureMonkey.giveMe(OrderItemRequest.class, 1);

        List<OrderItemRequest> itemList = List.of(
                new OrderItemRequest(1L, BigInteger.valueOf(49900), 1L)
        );

        return fixtureMonkey.giveMeBuilder(OrderRequest.class)
                .set("userId", 1L)
                .set("orderItems", itemList)
                .sample();
    }

    public static OrderRequest orderRequestId1WithTwoProduct() {
        List<OrderItemRequest> orderItems = fixtureMonkey.giveMe(OrderItemRequest.class, 1);

        List<OrderItemRequest> itemList = List.of(
                new OrderItemRequest(1L, BigInteger.valueOf(49900), 1L),
                new OrderItemRequest(3L, BigInteger.valueOf(30000), 2L)
        );

        return fixtureMonkey.giveMeBuilder(OrderRequest.class)
                .set("userId", 1L)
                .set("orderItems", itemList)
                .sample();
    }

    public static OrderRequest orderRequestId1WithTwoProduct2() {
        List<OrderItemRequest> orderItems = fixtureMonkey.giveMe(OrderItemRequest.class, 1);

        List<OrderItemRequest> itemList = List.of(
                new OrderItemRequest(1L, BigInteger.valueOf(49900), 1L),
                new OrderItemRequest(5L, BigInteger.valueOf(30000), 2L)
        );

        return fixtureMonkey.giveMeBuilder(OrderRequest.class)
                .set("userId", 1L)
                .set("orderItems", itemList)
                .sample();
    }


    public static Order orderUserId1WithOneItem() {
        List<OrderItem> orderItems = List.of(
                new OrderItem(1L, "바람막이", BigInteger.valueOf(49900), BigInteger.valueOf(49900), 1L, OrderItemStatus.PREPARING)
        );

        return fixOrderId()
                .set("userId", 1L)
                .set("orderItems", orderItems)
                .set("paymentType", "POINT")
                .set("totalPrice", BigInteger.valueOf(49900))
                .set("orderStatus", OrderStatus.PENDING)
                .sample();
    }

    public static Order orderUserId1WithTwoItem() {
        List<OrderItem> orderItems = List.of(
                new OrderItem(1L, "바람막이", BigInteger.valueOf(49900), BigInteger.valueOf(49900), 1L, OrderItemStatus.PREPARING),
                new OrderItem(3L, "아노락", BigInteger.valueOf(40000), BigInteger.valueOf(80000), 2L, OrderItemStatus.PREPARING)
        );

        return fixOrderId()
                .set("userId", 1L)
                .set("orderItems", orderItems)
                .set("paymentType", "POINT")
                .set("totalPrice", BigInteger.valueOf(129900))
                .set("orderStatus", OrderStatus.PENDING)
                .sample();
    }

    public static Order orderNegativeOrderPrice() {
        List<OrderItem> orderItems = List.of(
                new OrderItem(1L, "바람막이", BigInteger.valueOf(49900), BigInteger.valueOf(49900), 1L, OrderItemStatus.PREPARING),
                new OrderItem(3L, "아노락", BigInteger.valueOf(40000), BigInteger.valueOf(80000), 2L, OrderItemStatus.PREPARING)
        );

        return fixOrderId()
                .set("userId", 1L)
                .set("orderItems", orderItems)
                .set("paymentType", "POINT")
                .set("totalPrice", BigInteger.valueOf(-10000))
                .set("orderStatus", OrderStatus.PENDING)
                .sample();
    }

    public static Order orderNotPendingStatus() {
        List<OrderItem> orderItems = List.of(
                new OrderItem(1L, "바람막이", BigInteger.valueOf(49900), BigInteger.valueOf(49900), 1L, OrderItemStatus.PREPARING)
        );

        return fixOrderId()
                .set("userId", 1L)
                .set("orderItems", orderItems)
                .set("paymentType", "POINT")
                .set("totalPrice", BigInteger.valueOf(49900))
                .set("orderStatus", OrderStatus.PAID)
                .sample();
    }

    public static Payment paymentOrder(User user, Order order) {
        return fixPointPayment()
                .set("orderId", order.getOrderId())
                .set("userId", user.getUserId())
                .set("point", order.getTotalPrice())
                .set("status", "PENDING")
                .set("paidAt", LocalDateTime.now())
                .sample();
    }

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

    public static List<OrderItem> requestDecreaseStockForId1One() {
        return OrderSheet.from(orderRequestId1With49900PriceProduct()).getOrderItems();
    }
    public static List<OrderItem> requestDecreaseStockForId1AndId2() {
        return OrderSheet.from(orderRequestId1WithTwoProduct2()).getOrderItems();
    }
}