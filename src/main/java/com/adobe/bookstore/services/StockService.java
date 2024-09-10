package com.adobe.bookstore.services;

import java.util.List;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.adobe.bookstore.models.BookStock;
import com.adobe.bookstore.models.OrderItem;




import com.adobe.bookstore.repositories.BookStockRepository;

/**
 * Service for managing book stock updates.
 * This service handles asynchronous updates to the stock of books after an order is placed.
 */
@Service
public class StockService {

    private final BookStockRepository bookRepository;
   
    /**
     * Constructs a StockService with the specified BookStockRepository.
     *
     * @param bookRepository The repository used to access and manage book stock data.
     */
    public StockService(BookStockRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    /**
     * Asynchronously updates the stock of books based on the provided list of order items.
     * This method reduces the quantity of each book in the stock according to the quantities specified in the order items.
     * If an error occurs during the update, it is logged, but the order processing is not affected.
     * 
     * @param orderItems The list of {@link OrderItem} objects representing the items in the order.
     * @throws Exception 
     */
    @Async
    public void updateStockAsync(List<OrderItem> orderItems) throws Exception {
        
        try {
            for (OrderItem item : orderItems) {
                // Retrieve the book from the repository
                BookStock book = bookRepository.findById(item.getBook().getId()).orElseThrow();
                // Reduce the stock quantity of the book
                book.reduceStock(item.getQuantity());
                // Save the updated book stock
                bookRepository.save(book);
            }
        } catch (Exception e) {
            // Log the error, but do not affect the order processing
            throw new Exception("Error updating stock asynchronously" , e);
        } 
      
    }
}
