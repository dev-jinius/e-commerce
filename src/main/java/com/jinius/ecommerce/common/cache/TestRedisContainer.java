package com.jinius.ecommerce;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
public abstract class TestRedisContainer {

    private static final String REDIS = "redis:latest";

    @Container
    static final GenericContainer REDIS_CONTAINER = new GenericContainer<>(DockerImageName.parse("redis:latest"))
            .withExposedPorts(6379)
            .withEnv("REDIS_PASSWORD", "redis")
            .withReuse(true);
}
