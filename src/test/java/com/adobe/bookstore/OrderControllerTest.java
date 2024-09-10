package com.adobe.bookstore;

import com.adobe.bookstore.controllers.OrderController;
import com.adobe.bookstore.models.BookStock;
import com.adobe.bookstore.models.Order;
import com.adobe.bookstore.models.OrderItem;
import com.adobe.bookstore.services.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the {@link OrderController}.
 * This test class ensures the proper functioning of the OrderController
 * by mocking the dependencies and simulating different scenarios.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    /**
     * Initializes mocks before each test method is executed.
     */
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    /**
     * Test to verify successful order creation.
     * This test simulates a scenario where the order is created successfully,
     * checks if the response status is OK (200), and verifies the response body.
     *
     * @throws Exception in case of unexpected errors.
     */
    @Test
    public void shouldCreateOrderSuccessfully() throws Exception {
        // Given
        BookStock book1 = new BookStock("book1", "Book One", 10);
        BookStock book2 = new BookStock("book2", "Book Two", 5);

        OrderItem orderItem1 = new OrderItem(book1, 2);
        OrderItem orderItem2 = new OrderItem(book2, 1);
        List<OrderItem> orderItems = Arrays.asList(orderItem1, orderItem2);

        UUID mockOrderId = UUID.randomUUID();

        // Mocking service behavior
        when(orderService.createOrder(orderItems)).thenReturn(mockOrderId);

        // When
        ResponseEntity<?> response = orderController.createOrder(orderItems);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Order created with ID: " + mockOrderId);

        verify(orderService, times(1)).createOrder(orderItems);
    }

    /**
     * Test to verify the behavior when the order creation fails due to insufficient stock.
     * This test simulates a scenario where the order fails, ensuring the controller
     * returns a Bad Request (400) status and the appropriate error message.
     *
     * @throws Exception in case of unexpected errors.
     */
    @Test
    public void shouldReturnBadRequestWhenOrderFails() throws Exception {
        // Given
        BookStock book1 = new BookStock("book1", "Book One", 10);
        OrderItem orderItem1 = new OrderItem(book1, 20); // Pedido mayor al stock disponible
        List<OrderItem> orderItems = Arrays.asList(orderItem1);

        // Simulating exception in the service
        when(orderService.createOrder(orderItems)).thenThrow(new Exception("Insufficient stock"));

        // When
        ResponseEntity<?> response = orderController.createOrder(orderItems);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo("Insufficient stock");

        verify(orderService, times(1)).createOrder(orderItems);
    }

      /**
     * Test to verify successful retrieval of existing orders.
     * This test simulates a scenario where orders are successfully retrieved
     * and checks if the response status is OK (200) and the response body contains the list of orders.
     *
     * @throws Exception in case of unexpected errors.
     */
    @Test
    public void shouldReturnOrdersSuccessfully() throws Exception {
        // Given
        Order order1 = new Order();
        Order order2 = new Order();
        List<Order> mockOrders = Arrays.asList(order1, order2);

        // Mocking service behavior
        when(orderService.getAll()).thenReturn(mockOrders);

        // When
        ResponseEntity<List<Order>> response = orderController.getOrderExist();

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(mockOrders);

        verify(orderService, times(1)).getAll();
    }


    /**
     * Test to verify the behavior when retrieving orders fails.
     * This test simulates a scenario where the retrieval of orders fails, ensuring
     * the controller returns a Bad Request (400) status and an empty body.
     *
     * @throws Exception in case of unexpected errors.
     */
    @Test
    public void shouldReturnBadRequestWhenRetrievingOrdersFails() throws Exception {
        // Simulating exception in the service
        when(orderService.getAll()).thenThrow(new Exception("Error retrieving orders"));

        // When
        ResponseEntity<List<Order>> response = orderController.getOrderExist();

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNull();

        verify(orderService, times(1)).getAll();
    }
}