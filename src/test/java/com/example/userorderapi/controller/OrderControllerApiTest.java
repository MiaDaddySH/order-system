package com.example.userorderapi.controller;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.userorderapi.exception.GlobalExceptionHandler;
import com.example.userorderapi.dto.OrderResponse;
import com.example.userorderapi.mapper.OrderMapper;
import com.example.userorderapi.model.Order;
import com.example.userorderapi.service.OrderService;

class OrderControllerApiTest {

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        OrderMapper orderMapper = new OrderMapper() {
            @Override
            public OrderResponse toOrderResponse(Order order) {
                return new OrderResponse(order.getId(), order.getProduct(), order.getQuantity(), order.getStatus());
            }

            @Override
            public List<OrderResponse> toOrderResponses(List<Order> orders) {
                return orders.stream().map(this::toOrderResponse).toList();
            }
        };
        OrderController orderController = new OrderController(orderMapper, new OrderService());
        mockMvc = MockMvcBuilders.standaloneSetup(orderController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void createOrderReturnsOrderWhenRequestIsValid() throws Exception {
        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "product": "Book",
                                  "quantity": 2
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/orders/1001"))
                .andExpect(jsonPath("$.id", greaterThanOrEqualTo(1001)))
                .andExpect(jsonPath("$.product").value("Book"))
                .andExpect(jsonPath("$.quantity").value(2))
                .andExpect(jsonPath("$.status").value("created"));
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
        mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "product": "Book",
                          "quantity": 2
                        }
                        """));
        mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "product": "Pen",
                          "quantity": 3
                        }
                        """));

        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].product").value("Book"))
                .andExpect(jsonPath("$[1].product").value("Pen"));
    }

    @Test
    void getOrderReturnsNotFoundWhenOrderDoesNotExist() throws Exception {
        mockMvc.perform(get("/orders/999999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Order not found"));
    }

    @Test
    void deleteOrderReturnsDeletedMessageWhenOrderExists() throws Exception {
        mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "product": "Book",
                          "quantity": 2
                        }
                        """));

        mockMvc.perform(delete("/orders/1001"))
                .andExpect(status().isOk())
                .andExpect(content().string("Deleted order 1001"));
    }

    @Test
    void deleteOrderReturnsNotFoundWhenOrderMissing() throws Exception {
        mockMvc.perform(delete("/orders/999999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Order not found"));
    }
}
