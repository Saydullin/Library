package com.example.wt_bookshop.model.entities.stock;

import com.example.wt_bookshop.model.entities.book.Book;

public class Stock {
    private Book book;
    private Integer stock;
    private Integer reserved;

    public Book getPhone() {
        return book;
    }

    public void setPhone(Book book) {
        this.book = book;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getReserved() {
        return reserved;
    }

    public void setReserved(Integer reserved) {
        this.reserved = reserved;
    }

}
