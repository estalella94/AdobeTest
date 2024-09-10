package com.adobe.bookstore.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;



/**
 * Represents an item in an order.
 * Each order item is associated with a specific book and contains the quantity ordered.
 */
@Entity
public class OrderItem {

     /**
     * Unique identifier for the order item, automatically generated.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

     /**
     * The book associated with this order item.
     * This is a many-to-one relationship because multiple order items can refer to the same book.
     */
    @ManyToOne
    private BookStock book;

    /**
     * The quantity of the book ordered in this item.
     */
    private int quantity;

    /**
     * Default constructor.
     * Required by JPA.
     */
    public OrderItem() {}

     /**
     * Constructor that initializes the order item with a book and the quantity ordered.
     *
     * @param book The book associated with this order item.
     * @param quantity The quantity of the book ordered.
     */
    public OrderItem(BookStock book, int quantity) {
        this.book = book;
        this.quantity = quantity;
    }

    /**
     * Getters and Setters.
     *
     */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BookStock getBook() {
        return book;
    }

    public void setBook(BookStock book) {
        this.book = book;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
