package com.adobe.bookstore.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.adobe.bookstore.models.BookStock;
import com.adobe.bookstore.repositories.BookStockRepository;

/**
 * Controller for managing book stock information.
 * This controller handles HTTP requests related to book stock, including retrieving stock details for a specific book.
 */
@RestController
@RequestMapping("/books_stock/")
public class BookStockResource {
    
    
    private BookStockRepository bookStockRepository;

    /**
     * Constructs a BookStockResource with the specified BookStockRepository.
     *
     * @param bookStockRepository The repository used to access book stock data.
     */
    public BookStockResource(BookStockRepository bookStockRepository) {
        this.bookStockRepository = bookStockRepository;
    }
    
    /**
     * Endpoint to retrieve stock information for a specific book by its ID.
     * 
     * @param bookId The ID of the book whose stock information is to be retrieved.
     * @return A response entity containing the book stock information if found, or a 404 Not Found status if the book does not exist.
     */
    @GetMapping("{bookId}")
    public ResponseEntity<BookStock> getStockById(@PathVariable String bookId) {
        return bookStockRepository.findById(bookId)
                .map(bookStock -> ResponseEntity.ok(bookStock))
                .orElse(ResponseEntity.notFound().build());
    }
}
