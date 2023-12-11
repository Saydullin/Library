package com.example.wt_bookshop.model.entities.order;

import com.example.wt_bookshop.model.dao.BookDao;
import com.example.wt_bookshop.model.dao.impl.JdbcBookDao;
import com.example.wt_bookshop.model.exceptions.DaoException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderItemsExtractor {
    public List<OrderItem> extractData(ResultSet resultSet) throws SQLException, DaoException {
        List<OrderItem> orderItems = new ArrayList<>();
        BookDao bookDao = JdbcBookDao.getInstance();
        while (resultSet.next()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setBook(bookDao.get(resultSet.getLong("bookId")).orElse(null));
            orderItem.setQuantity(resultSet.getInt("quantity"));
            orderItems.add(orderItem);
        }
        return orderItems;
    }
}
