package com.example.wt_bookshop.model.entities.book;

import com.example.wt_bookshop.model.entities.genre.Genre;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Book {
    private Long id;
    private String bookName;
    private String author;
    private BigDecimal price;
    private String releaseYear;
    private String publishing;
    private Set<Genre> genres = new HashSet<>();
    private String imageUrl;
    private String description;

    public Book() {
    }

    public Book(Long id, String bookName, String author, BigDecimal price, String releaseYear, String publishing, Set<Genre> genres,  String imageUrl, String description) {
        this.id = id;
        this.bookName = bookName;
        this.author = author;
        this.price = price;
        this.genres = genres;
        this.releaseYear = releaseYear;
        this.publishing = publishing;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    public Book(Long id, String bookName, String author, BigDecimal price, String imageUrl) {
        this.id = id;
        this.bookName = bookName;
        this.author = author;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String b) {
        this.bookName = b;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String a) {
        this.author = a;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getReleaseYear(){return releaseYear;}
    public void setReleaseYear(String r){this.releaseYear = r;}

    public String getPublishing(){return this.publishing;}
    public void setPublishing(String publishing){this.publishing = publishing;}

    public Set<Genre> getGenres() {
        return genres;
    }

    public void setGenres(Set<Genre> genres) {
        this.genres = genres;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return id.equals(book.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
