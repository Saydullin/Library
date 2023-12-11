package com.example.wt_bookshop.model.dao;

import com.example.wt_bookshop.model.exceptions.DaoException;

/**
 * @author sasha
 * @version 1.0
 */
public interface StockDao {
    /**
     * Find available stock of book
     *
     * @param bookId id of book
     * @return available stock
     * @throws DaoException throws when there is some errors during dao method execution
     */
    Integer availableStock(Long bookId) throws DaoException;

    /**
     * Update reserve of books in database
     *
     * @param bookId  - book to update
     * @param quantity - quantity to add in reserve field
     * @throws DaoException throws when there is some errors during dao method execution
     */
    void reserve(Long bookId, int quantity) throws DaoException;
}
