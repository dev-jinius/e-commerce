package com.jinius.ecommerce;

import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public abstract class TestContainer extends TestRedisContainer {

    private static final String MARIA_DB = "mariadb:10.6.18";

    static final MariaDBContainer MARIADB_CONTAINER = new MariaDBContainer<>(MARIA_DB)
            .withUsername("test")
            .withPassword("test")
            .withDatabaseName("jinius")
            .withInitScript("init.sql");

            ;
}
