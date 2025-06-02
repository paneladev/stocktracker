package com.pdev.stocktracker.controller;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MongoDBContainer;

@SpringBootTest
@AutoConfigureMockMvc
public class BaseIntegrationTests {

    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @BeforeAll
    static void beforeAll() {
        mongoDBContainer.start();
    }

    @Autowired
    protected MockMvc mockMvc;
}
