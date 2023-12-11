package com.example.wt_bookshop.model.entities.order;

import com.example.wt_bookshop.model.entities.book.Book;

public class OrderItem {
    private Long id;
    private Book book;
    private Order order;
    private int quantity;

    public Book getBook() {
        return book;
    }

    public void setBook(final Book book) {
        this.book = book;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(final Order order) {
        this.order = order;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(final int quantity) {
        this.quantity = quantity;
    }
}