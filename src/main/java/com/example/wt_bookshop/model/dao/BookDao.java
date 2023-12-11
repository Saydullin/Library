package com.example.wt_bookshop.model.dao;

import com.example.wt_bookshop.model.entities.book.Book;
import com.example.wt_bookshop.model.enums.SortField;
import com.example.wt_bookshop.model.enums.SortOrder;
import com.example.wt_bookshop.model.exceptions.DaoException;

import java.util.List;
import java.util.Optional;

/**
 * @author sasha
 * @version 1.0
 */
public interface BookDao {
    /**
     * Find book by id
     *
     * @param key id of phone
     * @return book with id
     * @throws DaoException throws when there is some errors during dao method execution
     */
    Optional<Book> get(Long key) throws DaoException;

    /**
     * Find phones from database
     *
     * @param offset    - offset of found books
     * @param limit     - limit of found books
     * @param sortField - field to sort (bookName, author, price, releaseYear)
     * @param sortOrder - sort order (asc or desc)
     * @param query     - query for find
     * @return List of books
     * @throws DaoException throws when there is some errors during dao method execution
     */

    List<Book> findAll(int offset, int limit, SortField sortField, SortOrder sortOrder, String query) throws DaoException;

    /**
     * Number of founded phones
     *
     * @param query - query for find
     * @return number of phones
     * @throws DaoException throws when there is some errors during dao method execution
     */
    Long numberByQuery(String query) throws DaoException;
}
