package com.adobe.bookstore.services;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.adobe.bookstore.models.BookStock;
import com.adobe.bookstore.models.Order;
import com.adobe.bookstore.models.OrderItem;

import com.adobe.bookstore.repositories.BookStockRepository;
import com.adobe.bookstore.repositories.OrderRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


/**
 * Service for managing orders in the bookstore.
 * This service handles the creation of orders and retrieval of existing orders.
 */
@Service
public class OrderService {

    private final BookStockRepository bookRepository;
    private final OrderRepository orderRepository;
    private final StockService stockService;

    /**
     * Constructs an OrderService with the specified repositories and stock service.
     *
     * @param bookRepository The repository used to access book stock data.
     * @param orderRepository The repository used to manage orders.
     * @param stockService The service used to update book stock asynchronously.
     */
    public OrderService(BookStockRepository bookRepository, OrderRepository orderRepository, StockService stockService) {
        this.bookRepository = bookRepository;
        this.orderRepository = orderRepository;
        this.stockService = stockService;
    }

    /**
     * Creates a new order based on the provided list of order items.
     * This method validates stock availability for each book in the order. 
     * If stock is sufficient, the order is saved and stock is updated asynchronously.
     * 
     * @param orderItems The list of {@link OrderItem} objects representing the items to be ordered.
     * @return The unique identifier of the created order.
     * @throws Exception If there is insufficient stock for any of the books in the order.
     */
    @Transactional
    public UUID createOrder(List<OrderItem> orderItems) throws Exception {
       // Validate stock availability for each book in the order
        for (OrderItem item : orderItems) {
            BookStock book = item.getBook();
            Optional<BookStock> bookOptional = bookRepository.findById(book.getId());
            if (bookOptional.isEmpty() || bookOptional.get().getQuantity() < item.getQuantity()) {
                throw new Exception("Insufficient stock for the book: " + bookOptional.get().getName());
            }
        }
        // Save the order
        Order order = new Order(orderItems);
        Order savedOrder = orderRepository.save(order);

        // Update stock asynchronously
        stockService.updateStockAsync(orderItems);

        return savedOrder.getId();
    }

    /**
     * Retrieves all existing orders from the repository.
     * 
     * @return A list of {@link Order} objects representing all orders in the system.
     * @throws Exception If there is an issue retrieving the orders.
     */
    @Transactional
    public List<Order> getAll() throws Exception {
        return orderRepository.findAll();
    }




}
