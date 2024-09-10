package com.adobe.bookstore;

import com.adobe.bookstore.models.BookStock;
import com.adobe.bookstore.models.Order;
import com.adobe.bookstore.models.OrderItem;
import com.adobe.bookstore.services.OrderService;
import com.adobe.bookstore.repositories.BookStockRepository;
import com.adobe.bookstore.repositories.OrderRepository;
import com.adobe.bookstore.services.StockService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;


/**
 * Unit tests for the {@link OrderService} class.
 * This class uses Mockito for mocking dependencies and testing the functionality of the OrderService class.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private BookStockRepository bookRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private StockService stockService;

    /**
     * Initializes mocks before each test.
     */
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Tests the scenario where an order is created successfully with sufficient stock.
     * 
     * Given a list of order items and corresponding books with sufficient stock,
     * when the order is created, then the order ID is returned and the order is saved,
     * and the stock is updated asynchronously.
     * 
     * @throws Exception if there is an issue creating the order.
     */
    @Test
    @Transactional
    public void shouldCreateOrderSuccessfully() throws Exception {
        // Given
        BookStock book1 = new BookStock("book1", "Book One", 10);
        BookStock book2 = new BookStock("book2", "Book Two", 5);

        OrderItem orderItem1 = new OrderItem(book1, 2);
        OrderItem orderItem2 = new OrderItem(book2, 1);

        List<OrderItem> orderItems = Arrays.asList(orderItem1, orderItem2);

        when(bookRepository.findById("book1")).thenReturn(Optional.of(book1));
        when(bookRepository.findById("book2")).thenReturn(Optional.of(book2));
        when(orderRepository.save(any(Order.class))).thenReturn(new Order(orderItems));
        doNothing().when(stockService).updateStockAsync(any());

        // When
        UUID orderId = orderService.createOrder(orderItems);

        // Then
        assertThat(orderId).isNotNull();
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(stockService, times(1)).updateStockAsync(any());
    }

    /**
     * Tests the scenario where an order cannot be created due to insufficient stock.
     *
     * Given an order item with a book having insufficient stock,
     * when the order is attempted to be created, then an exception is thrown
     * and the order repository save method is not called.
     * 
     * @throws Exception if there is an issue retrieving the book or creating the order.
     */
    @Test
    public void shouldThrowExceptionWhenStockIsInsufficient() {
        // Given
        BookStock book = new BookStock("book1", "Book One", 1);
        OrderItem orderItem = new OrderItem(book, 2);

        when(bookRepository.findById("book1")).thenReturn(Optional.of(book));

        // When / Then
        Exception exception = null;
        try {
            orderService.createOrder(Arrays.asList(orderItem));
        } catch (Exception e) {
            exception = e;
        }

        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).contains("Insufficient stock for the book");
        verify(orderRepository, never()).save(any(Order.class));
    }
}
