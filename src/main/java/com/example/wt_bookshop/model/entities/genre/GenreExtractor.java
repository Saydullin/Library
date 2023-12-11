package com.example.wt_bookshop.model.entities.genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GenreExtractor {
    public List<Genre> extractData(ResultSet resultSet) throws SQLException {
        List<Genre> genres = new ArrayList<>();

        while (resultSet.next()) {
            Genre genre = new Genre();
            genre.setId(resultSet.getLong("ID"));
            genre.setCode(resultSet.getString("CODE"));
            genres.add(genre);
        }
        return genres;
    }
}
