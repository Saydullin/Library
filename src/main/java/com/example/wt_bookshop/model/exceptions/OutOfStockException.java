package com.example.wt_bookshop.model.exceptions;

import com.example.wt_bookshop.model.entities.book.Book;

/**
 * @author sasha
 * @version 1.0
 */
public class OutOfStockException extends Exception {
    /**
     * Phone that outOfStock
     */
    private Book book;
    /**
     * Requested stock of phone
     */
    private int requestedStock;
    /**
     * Available stock of phone
     */
    private int availableStock;

    /**
     * Constructor of exception
     * @param book phone of exception
     * @param requestedStock requested stock of exception
     * @param availableStock available stock of exceptttion
     */
    public OutOfStockException(Book book, int requestedStock, int availableStock) {
        this.book = book;
        this.requestedStock = requestedStock;
        this.availableStock = availableStock;
    }

    /**
     * Place exception message
     * @param s exception message
     */
    public OutOfStockException(String s) {
        super(s);
    }

    public Book getProduct() {
        return book;
    }

    public int getRequestedStock() {
        return requestedStock;
    }

    public int getAvailableStock() {
        return availableStock;
    }

}
