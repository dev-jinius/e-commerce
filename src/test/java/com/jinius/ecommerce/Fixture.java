package com.jinius.ecommerce;

import com.jinius.ecommerce.order.api.OrderItemRequest;
import com.jinius.ecommerce.order.api.OrderRequest;
import com.jinius.ecommerce.order.domain.Order;
import com.jinius.ecommerce.order.domain.OrderItem;
import com.jinius.ecommerce.order.domain.OrderItemStatus;
import com.jinius.ecommerce.order.domain.OrderStatus;
import com.jinius.ecommerce.user.api.UserChargeRequest;
import com.jinius.ecommerce.user.domain.User;
import com.navercorp.fixturemonkey.ArbitraryBuilder;
import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.FieldReflectionArbitraryIntrospector;

import java.math.BigInteger;
import java.util.List;

import static java.time.LocalTime.now;

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
        List<OrderItemRequest> orderItems = fixtureMonkey.giveMe(OrderItemRequest.class, 1);

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
}