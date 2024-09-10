package com.adobe.bookstore.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.adobe.bookstore.models.OrderItem;
import com.adobe.bookstore.services.OrderService;
import com.adobe.bookstore.models.Order;


/**
 * Controller for managing orders in the bookstore.
 * This controller handles HTTP requests related to orders, including creating new orders and retrieving existing ones.
 */
@RestController
@RequestMapping("/orders")
public class OrderController {
 private final OrderService orderService;

   /**
     * Constructs an OrderController with the specified OrderService.
     *
     * @param orderService The service used to handle order-related operations.
     */
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }



    /**
     * Endpoint to create a new order.
     * Receives a list of {@link OrderItem} objects from the request body, processes the order, and returns the order ID.
     * 
     * @param orderItems The list of order items to be processed.
     * @return A response entity with the order creation status and the unique order ID if successful.
     */
    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody List<OrderItem> orderItems) {
        try {
            UUID orderId = orderService.createOrder(orderItems);
            return ResponseEntity.ok().body("Order created with ID: " + orderId);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Endpoint to retrieve the list of existing orders.
     * Returns a list of {@link Order} objects representing all orders in the system.
     * 
     * @return A response entity containing the list of orders if successful, or a bad request status if an error occurs.
     */
    @GetMapping
    public ResponseEntity<List<Order>> getOrderExist() {
        try {
            
            List<Order> orders = orderService.getAll();
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
