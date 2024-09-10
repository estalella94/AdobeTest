package com.adobe.bookstore.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.adobe.bookstore.models.BookStock;


/**
 * Repository interface for managing {@link BookStock} entities.
 * This interface provides methods for performing CRUD operations and querying the book stock data.
 * 
 * The {@link JpaRepository} interface provides standard methods for interacting with the database.
 * The generic types specify that this repository manages {@link BookStock} entities and uses
 * a String as the identifier type.
 */
@Repository
public interface BookStockRepository extends JpaRepository<BookStock, String> {
}
