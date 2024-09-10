package com.adobe.bookstore;

import com.adobe.bookstore.models.BookStock;
import com.adobe.bookstore.models.OrderItem;
import com.adobe.bookstore.repositories.BookStockRepository;
import com.adobe.bookstore.services.StockService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


/**
 * Unit tests for the {@link StockService}.
 * This class contains tests to verify the behavior of the StockService,
 * including stock updates and error handling.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StockServiceTest {

    @Mock
    private BookStockRepository bookRepository;

    @InjectMocks
    private StockService stockService;

    /**
     * Initializes mocks before each test method is executed.
     */
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

      /**
     * Test to verify successful stock update.
     * This test simulates a scenario where stock is updated successfully
     * and verifies that the stock is reduced correctly and saved to the repository.
     * 
     * @throws Exception if any unexpected error occurs.
     */
    @Test
    public void shouldUpdateStockSuccessfully() throws Exception {
        // Given
        BookStock book1 = new BookStock("book1", "Book One", 10);
        BookStock book2 = new BookStock("book2", "Book Two", 5);

        OrderItem orderItem1 = new OrderItem(book1, 2);
        OrderItem orderItem2 = new OrderItem(book2, 1);

        List<OrderItem> orderItems = Arrays.asList(orderItem1, orderItem2);

        when(bookRepository.findById("book1")).thenReturn(Optional.of(book1));
        when(bookRepository.findById("book2")).thenReturn(Optional.of(book2));

        // When
        stockService.updateStockAsync(orderItems);

        // Then
        verify(bookRepository, times(1)).save(book1);
        verify(bookRepository, times(1)).save(book2);
        verify(bookRepository, times(1)).findById("book1");
        verify(bookRepository, times(1)).findById("book2");

        // Ensure that stock is reduced correctly
        assert book1.getQuantity() == 8;
        assert book2.getQuantity() == 4;
    }

    /**
     * Test to verify the behavior when a book is not found in the repository.
     * This test simulates a scenario where a book is not found when attempting
     * to update the stock, ensuring that an exception is thrown and no stock is saved.
     * 
     * @throws Exception if any unexpected error occurs.
     */
    @Test
    public void shouldThrowExceptionWhenBookNotFound() throws Exception {
        // Given
        OrderItem orderItem = new OrderItem(new BookStock("book1", "Book One", 10), 2);
        List<OrderItem> orderItems = Arrays.asList(orderItem);

        when(bookRepository.findById("book1")).thenReturn(Optional.empty());

        // When / Then
        Exception exception = null;
        try {
            stockService.updateStockAsync(orderItems);
        } catch (Exception e) {
            exception = e;
        }

        // Verify that the exception is not null and contains the appropriate message
        assert exception != null;
        assert exception.getMessage().contains("Error updating stock asynchronously");

        // Verify that the save method was not called because the book was not found
        verify(bookRepository, never()).save(any(BookStock.class));
    }

     /**
     * Test to verify the behavior when saving the updated stock fails.
     * This test simulates a scenario where saving the book stock fails due to a
     * database error, ensuring that an exception is thrown and appropriate error message is returned.
     * 
     * @throws Exception if any unexpected error occurs.
     */
    @Test
    public void shouldThrowExceptionWhenSaveFails() throws Exception {
        // Given
        BookStock book = new BookStock("book1", "Book One", 10);
        OrderItem orderItem = new OrderItem(book, 5);
        List<OrderItem> orderItems = Arrays.asList(orderItem);

        when(bookRepository.findById("book1")).thenReturn(Optional.of(book));
        doThrow(new RuntimeException("Database error")).when(bookRepository).save(any(BookStock.class));

        // When / Then
        Exception exception = null;
        try {
            stockService.updateStockAsync(orderItems);
        } catch (Exception e) {
            exception = e;
        }

        // Verify that an exception was thrown during the save operation
        assert exception != null;
        assert exception.getMessage().contains("Error updating stock asynchronously");

        // Verify that findById was called and save failed
        verify(bookRepository, times(1)).findById("book1");
        verify(bookRepository, times(1)).save(book);
    }
}