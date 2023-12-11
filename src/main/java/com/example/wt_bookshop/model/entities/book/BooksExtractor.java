package com.example.wt_bookshop.model.entities.book;

import com.example.wt_bookshop.model.dao.GenreDao;
import com.example.wt_bookshop.model.dao.impl.JdbcGenreDao;
import com.example.wt_bookshop.model.entities.genre.Genre;
import com.example.wt_bookshop.model.exceptions.DaoException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BooksExtractor {
    GenreDao genreDao = new JdbcGenreDao();

    public List<Book> extractData(ResultSet resultSet) throws SQLException, DaoException {
        List<Book> books = new ArrayList<>();
        while (resultSet.next()) {
            Book book = new Book();
            book.setId(resultSet.getLong("ID"));
            book.setBookName(resultSet.getString("BOOKNAME"));
            book.setAuthor(resultSet.getString("AUTHOR"));
            book.setPrice(resultSet.getBigDecimal("PRICE"));
            book.setReleaseYear(resultSet.getString("RELEASEYEAR"));
            book.setPublishing(resultSet.getString("PUBLISHING"));
            book.setImageUrl(resultSet.getString("IMAGEURL"));
            book.setDescription(resultSet.getString("DESCRIPTION"));
            List<Genre> genreList = genreDao.getColors(book.getId());
            Set<Genre> genres;
            if (genreList == null) {
                genres = null;
            } else {
                genres = new HashSet<>(genreList);
            }
            book.setGenres(genres);
            books.add(book);
        }

        return books;
    }
}
