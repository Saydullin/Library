package com.example.wt_bookshop.model.dao;

import com.example.wt_bookshop.model.entities.order.OrderItem;
import com.example.wt_bookshop.model.exceptions.DaoException;

import java.util.List;

/**
 * @author sasha
 * @version 1.0
 */
public interface OrderItemDao {
    /**
     * Find items from order
     *
     * @param key key of order
     * @return List of OrderItems from order
     * @throws DaoException throws when there is some errors during dao method execution
     */
    List<OrderItem> getOrderItems(Long key) throws DaoException;
}
