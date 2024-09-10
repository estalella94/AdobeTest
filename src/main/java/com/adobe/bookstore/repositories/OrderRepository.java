package com.adobe.bookstore.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.adobe.bookstore.models.Order;


/**
 * Repository interface for managing {@link Order} entities.
 * This interface provides methods for performing CRUD operations and querying order data.
 * 
 * The {@link JpaRepository} interface provides standard methods for interacting with the database.
 * The generic types specify that this repository manages {@link Order} entities and uses
 * a UUID as the identifier type.
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, UUID>{

}
