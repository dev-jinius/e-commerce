package com.jinius.ecommerce;

import com.jinius.ecommerce.order.api.OrderItemRequest;
import com.jinius.ecommerce.order.api.OrderRequest;
import com.jinius.ecommerce.user.domain.User;
import com.navercorp.fixturemonkey.ArbitraryBuilder;
import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.ConstructorPropertiesArbitraryIntrospector;

import java.math.BigInteger;
import java.util.List;

public class Fixture {

    public static FixtureMonkey fixtureMonkey = FixtureMonkey.builder()
            .objectIntrospector(ConstructorPropertiesArbitraryIntrospector.INSTANCE)
            .build();

    private static ArbitraryBuilder<User> fixUserName() {
        return fixtureMonkey.giveMeBuilder(User.class)
                .set("name", "이리스");
    }

    public static User id1With5000Point() {
        return fixUserName()
                .set("id", 1L)
                .set("point", BigInteger.valueOf(50000))
                .sample();
    }

    public static User id100With5000Point() {
        return fixUserName()
                .set("id", 100L)
                .set("point", BigInteger.valueOf(50000))
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
}
