package com.example.wt_bookshop.model.entities.cart;

import com.example.wt_bookshop.model.entities.book.Book;
import com.example.wt_bookshop.model.exceptions.CloneException;

import java.io.Serializable;

public class CartItem implements Serializable, Cloneable {
    private static final long serialVersionUID = 1L;
    private Book book;
    private int quantity;

    public CartItem(Book product, int quantity) {
        this.book = product;
        this.quantity = quantity;
    }

    public Book getBook() {
        return book;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setBook(Book product) {
        this.book = product;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "code=" + book.getId() +
                ", quantity=" + quantity;
    }

    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new CloneException("Error copying the product " + book.getId() + "with quantity" + quantity);
        }
    }
}
