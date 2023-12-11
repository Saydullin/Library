package com.example.wt_bookshop.model.entities.genre;

import java.util.Objects;

public class Genre {
    private Long id;
    private String code;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Genre() {
    }

    public Genre(Long id, String code) {
        this.id = id;
        this.code = code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Genre genre = (Genre) o;
        return code.equals(genre.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }
}
