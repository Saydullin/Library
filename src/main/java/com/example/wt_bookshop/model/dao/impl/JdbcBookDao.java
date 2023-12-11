package com.example.wt_bookshop.model.dao.impl;

import com.example.wt_bookshop.model.dao.BookDao;
import com.example.wt_bookshop.model.entities.book.Book;
import com.example.wt_bookshop.model.entities.book.BooksExtractor;
import com.example.wt_bookshop.model.enums.SortField;
import com.example.wt_bookshop.model.enums.SortOrder;
import com.example.wt_bookshop.model.exceptions.DaoException;
import com.example.wt_bookshop.model.utils.ConnectionPool;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Using jdbc to work with books
 *
 * @author sasha
 * @version 1.0
 */
public class JdbcBookDao implements BookDao {
    /**
     * Instance of logger
     */
    private static final Logger log = Logger.getLogger(BookDao.class);
    /**
     * Instance of bookExtractor
     */
    private BooksExtractor booksExtractor = new BooksExtractor();
    /**
     * Instance of BookDao
     */
    private static volatile BookDao instance;
    /**
     * Instance of ConnectionPool
     */
    private ConnectionPool connectionPool = ConnectionPool.getInstance();
    /**
     * SQL query to find phones by id
     */
    private static final String GET_QUERY = "SELECT * FROM books WHERE id = ?";
    /**
     * SQL query to find all books with available stock > 0, limit and offset
     */
    private static final String SIMPLE_FIND_ALL_QUERY = "select ph.* " +
            "from (select BOOKS.* from BOOKS " +
            "left join STOCKS on BOOKS.ID = STOCKS.BOOKID where STOCKS.STOCK - STOCKS.RESERVED > 0 and books.price > 0 offset ? limit ?) ph";
    /**
     * SQL query to find all books with available stock
     */
    private static final String FIND_WITHOUT_OFFSET_AND_LIMIT = "SELECT ph.* " +
            "FROM (SELECT books.* FROM books " +
            "LEFT JOIN stocks ON books.id = stocks.bookId WHERE stocks.stock - stocks.reserved > 0 ";

    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    /**
     * SQL query to find number of books
     */
    private static final String NUMBER_OF_PHONES_QUERY = "SELECT count(*) FROM BOOKS LEFT JOIN STOCKS ON BOOKS.ID = STOCKS.BOOKID WHERE STOCKS.STOCK - STOCKS.RESERVED > 0 AND books.price > 0";

    /**
     * Realisation of Singleton pattern
     *
     * @return instance of BookDao
     */
    public static BookDao getInstance() {
        if (instance == null) {
            synchronized (BookDao.class) {
                if (instance == null) {
                    instance = new JdbcBookDao();
                }
            }
        }
        return instance;
    }

    /**
     * Get book by id from database
     *
     * @param key id of book
     * @return book
     * @throws DaoException throws when there is some errors during dao method execution
     */
    @Override
    public Optional<Book> get(Long key) throws DaoException {
        Optional<Book> phone;
        Connection conn = null;
        PreparedStatement statement = null;
        try {
            lock.readLock().lock();
            conn = connectionPool.getConnection();
            statement = conn.prepareStatement(GET_QUERY);
            statement.setLong(1, key);
            ResultSet resultSet = statement.executeQuery();
            phone = booksExtractor.extractData(resultSet).stream().findAny();
            log.log(Level.INFO, "Found phones by id in the database");
        } catch (SQLException ex) {
            log.log(Level.ERROR, "Error in get function", ex);
            throw new DaoException("Error in process of getting phone");
        } finally {
            lock.readLock().unlock();
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    log.log(Level.ERROR, "Error in closing statement", ex);
                }
            }
            if (conn != null) {
                connectionPool.releaseConnection(conn);
            }
        }
        return phone;
    }

    /**
     * Find all books from database
     *
     * @param offset    - offset of found books
     * @param limit     - limit of found books
     * @param sortField - field to sort (bookName, author, price, releaseYear)
     * @param sortOrder - sort order (asc or desc)
     * @param query     - query for find
     * @return list of books
     * @throws DaoException throws when there is some errors during dao method execution
     */
    @Override
    public List<Book> findAll(int offset, int limit, SortField sortField, SortOrder sortOrder, String query) throws DaoException {
        List<Book> books;
        String sql = makeFindAllSQL(sortField, sortOrder, query);
        Connection conn = null;
        PreparedStatement statement = null;
        try {
            lock.readLock().lock();
            conn = connectionPool.getConnection();
            statement = conn.prepareStatement(sql);
            statement.setInt(1, offset);
            statement.setInt(2, limit);
            ResultSet resultSet = statement.executeQuery();
            books = booksExtractor.extractData(resultSet);
            log.log(Level.INFO, "Found all phones in the database");
        } catch (SQLException ex) {
            log.log(Level.ERROR, "Error in findAll", ex);
            throw new DaoException("Error in process of getting all phones");
        } finally {
            lock.readLock().unlock();
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    log.log(Level.ERROR, "Error in closing statement", ex);
                }
            }
            if (conn != null) {
                connectionPool.releaseConnection(conn);
            }
        }
        return books;
    }

    /**
     * Find number of books by query from database
     *
     * @param query - query for find
     * @return number of books
     * @throws DaoException throws when there is some errors during dao method execution
     */
    @Override
    public Long numberByQuery(String query) throws DaoException {
        String sql;
        if (query == null || query.equals("")) {
            sql = NUMBER_OF_PHONES_QUERY;
        } else {
            sql = NUMBER_OF_PHONES_QUERY + " AND " +
                    "(LOWER(BOOKS.BOOKNAME) LIKE LOWER('" + query + "%') " +
                    "OR LOWER(BOOKS.BOOKNAME) LIKE LOWER('% " + query + "%') " +
                    "OR LOWER(BOOKS.AUTHOR) LIKE LOWER('" + query + "%') " +
                    "OR LOWER(BOOKS.AUTHOR) LIKE LOWER('% " + query + "%'))";
        }
        Connection conn = null;
        Statement statement = null;
        try {
            lock.readLock().lock();
            conn = connectionPool.getConnection();
            statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            if (rs.next()) {
                return rs.getLong(1);
            }
            log.log(Level.INFO, "Found count of phones");
        } catch (SQLException ex) {
            log.log(Level.ERROR, "Error in numberByQuery", ex);
            throw new DaoException("Error in process of getting number of phones");
        } finally {
            lock.readLock().unlock();
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    log.log(Level.ERROR, "Error in closing statement", ex);
                }
            }
            if (conn != null) {
                connectionPool.releaseConnection(conn);
            }
        }
        return 0L;
    }

    /**
     * Make sql query with sorting and finding
     *
     * @param sortField - field to sort
     * @param sortOrder - order to sort
     * @param query     - query to find
     * @return sql query
     */
    private String makeFindAllSQL(SortField sortField, SortOrder sortOrder, String query) {
        if (sortField != null || query != null && !query.equals("")) {
            StringBuilder sql = new StringBuilder(FIND_WITHOUT_OFFSET_AND_LIMIT);

            if (query != null && !query.equals("")) {
                sql.append("AND (" + "LOWER(BOOKS.BOOKNAME) LIKE LOWER('").append(query).append("%') ").
                        append("OR LOWER(BOOKS.BOOKNAME) LIKE LOWER('% ").append(query).append("%') ").
                        append("OR LOWER(BOOKS.AUTHOR) LIKE LOWER('").append(query).append("%') ").
                        append("OR LOWER(BOOKS.AUTHOR) LIKE LOWER('% ").append(query).append("%')").append(") ");
            }
            sql.append("AND BOOKS.PRICE > 0 ");
            if (sortField != null) {
                sql.append("ORDER BY ").append(sortField.name()).append(" ");
                if (sortOrder != null) {
                    sql.append(sortOrder.name()).append(" ");
                } else {
                    sql.append("ASC ");
                }
            }
            sql.append("offset ? limit ?) ph");
            return sql.toString();
        } else {
            return SIMPLE_FIND_ALL_QUERY;
        }
    }
}
