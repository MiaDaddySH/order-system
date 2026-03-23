package com.example.userorderapi.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerApiTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("TRUNCATE TABLE orders RESTART IDENTITY CASCADE");
    }

    @Test
    void createOrderReturnsOrderWhenRequestIsValid() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "product": "Book",
                                  "quantity": 2
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.product").value("Book"))
                .andExpect(jsonPath("$.quantity").value(2))
                .andExpect(jsonPath("$.status").value("created"))
                .andReturn();

        int orderId = objectMapper.readTree(mvcResult.getResponse().getContentAsString()).get("id").asInt();
        String location = mvcResult.getResponse().getHeader("Location");

        mockMvc.perform(get("/orders/" + orderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(orderId));

        org.junit.jupiter.api.Assertions.assertEquals("/orders/" + orderId, location);
    }

    @Test
    void createOrderReturnsBadRequestWhenRequestIsMalformed() throws Exception {
        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createOrderReturnsBadRequestWhenRequestIsInvalid() throws Exception {
        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "product": "",
                                  "quantity": 1
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Product is required"));
    }

    @Test
    void createOrderReturnsBadRequestWhenQuantityIsNotPositive() throws Exception {
        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "product": "Book",
                                  "quantity": 0
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Quantity must be positive"));
    }

    @Test
    void getAllOrdersReturnsCreatedOrders() throws Exception {
        MvcResult firstCreate = mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "product": "Book",
                          "quantity": 2
                        }
                        """))
                .andExpect(status().isCreated())
                .andReturn();
        MvcResult secondCreate = mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "product": "Pen",
                          "quantity": 3
                        }
                        """))
                .andExpect(status().isCreated())
                .andReturn();

        int firstOrderId = objectMapper.readTree(firstCreate.getResponse().getContentAsString()).get("id").asInt();
        int secondOrderId = objectMapper.readTree(secondCreate.getResponse().getContentAsString()).get("id").asInt();

        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(firstOrderId))
                .andExpect(jsonPath("$[1].id").value(secondOrderId));
    }

    @Test
    void getOrderReturnsNotFoundWhenOrderDoesNotExist() throws Exception {
        mockMvc.perform(get("/orders/999999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Order not found"));
    }

    @Test
    void deleteOrderReturnsDeletedMessageWhenOrderExists() throws Exception {
        MvcResult createResult = mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "product": "Book",
                          "quantity": 2
                        }
                        """))
                .andReturn();

        int orderId = objectMapper.readTree(createResult.getResponse().getContentAsString()).get("id").asInt();

        mockMvc.perform(delete("/orders/" + orderId))
                .andExpect(status().isOk())
                .andExpect(content().string("Deleted order " + orderId));
    }

    @Test
    void deleteOrderReturnsNotFoundWhenOrderMissing() throws Exception {
        mockMvc.perform(delete("/orders/999999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Order not found"));
    }
}
