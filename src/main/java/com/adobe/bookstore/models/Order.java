package com.adobe.bookstore.models;

import java.util.List;
import java.util.UUID;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;


/**
 * Represents an order in the bookstore system.
 * Each order contains a unique identifier and a list of order items (books and quantities).
 */
@Entity
@Table(name = "orders")
public class Order {

    /**
     * Unique identifier for the order, automatically generated.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    /**
     * List of order items associated with the order.
     * Each item contains details such as the book and quantity ordered.
     * This relationship is managed with cascading operations, so if an order is saved,
     * the associated order items are also saved.
     */
    @OneToMany(cascade = CascadeType.ALL)
    private List<OrderItem> items;

    /**
     * Default constructor.
     * Required by JPA.
     */
    public Order() {}

    /**
     * Constructor that initializes the order with a list of order items.
     *
     * @param items List of order items that belong to this order.
     */
    public Order(List<OrderItem> items) {
        this.items = items;
    }

    /**
     * Getters and Setters.
     *
     */
    public List<OrderItem> getItems() {
        return items;
    }


    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
