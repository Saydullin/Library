package com.example.wt_bookshop.model.dao.impl;

import com.example.wt_bookshop.model.dao.GenreDao;
import com.example.wt_bookshop.model.entities.genre.Genre;
import com.example.wt_bookshop.model.entities.genre.GenreExtractor;
import com.example.wt_bookshop.model.exceptions.DaoException;
import com.example.wt_bookshop.model.utils.ConnectionPool;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Using jdbc to work with genres
 *
 * @author sasha
 * @version 1.0
 */
public class JdbcGenreDao implements GenreDao {
    /**
     * Field of logger
     */
    private static final Logger log = Logger.getLogger(GenreDao.class);
    /**
     * Extractor of genres
     */
    private final GenreExtractor colorExtractor = new GenreExtractor();
    /**
     * Instance of connection pool
     */
    private final ConnectionPool connectionPool = ConnectionPool.getInstance();
    /**
     * SQL query for find genres
     */
    private static final String GET_QUERY = "select GENRE.ID, GENRE.CODE " +
            "from (select * from BOOK2GENRE where BOOKID = ?) b2g " +
            "left join GENRE on b2g.GENREID = GENRE.ID order by GENRE.ID";
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    /**
     * Get colors from database
     *
     * @param id - id of book
     * @return List of genres
     * @throws DaoException throws when there is some errors during dao method execution
     */
    @Override
    public List<Genre> getColors(Long id) throws DaoException {
        List<Genre> genres;
        Connection conn = null;
        PreparedStatement statement = null;
        try {
            lock.readLock().lock();
            conn = connectionPool.getConnection();
            statement = conn.prepareStatement(GET_QUERY);
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            genres = colorExtractor.extractData(resultSet);
            log.log(Level.INFO, "Found colors in the database");
        } catch (SQLException ex) {
            log.log(Level.ERROR, "Error in getColors", ex);
            throw new DaoException("Error in process of getting colors");
        } finally {
            lock.readLock().unlock();
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    log.log(Level.ERROR, "Error in closing statement", ex);
                    throw new DaoException("Error in process getting colors");
                }
            }
            if (conn != null) {
                connectionPool.releaseConnection(conn);
            }
        }
        return genres;
    }

}
