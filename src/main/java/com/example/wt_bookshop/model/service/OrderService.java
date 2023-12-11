package com.example.wt_bookshop.model.service;

import com.example.wt_bookshop.model.entities.cart.Cart;
import com.example.wt_bookshop.model.entities.order.Order;
import com.example.wt_bookshop.model.entities.order.OrderStatus;
import com.example.wt_bookshop.model.exceptions.OutOfStockException;
import com.example.wt_bookshop.model.exceptions.ServiceException;
import jakarta.servlet.http.HttpSession;

import java.util.Optional;

/**
 * @author sasha
 * @version 1.0
 */
public interface OrderService {
    /**
     * Create empty order
     * @param cart cart with items
     * @return order
     */
    Order createOrder(Cart cart);

    /**
     * Place order in database
     * @param order order to place
     * @param session session with cart
     * @throws OutOfStockException throws when some product out of stock when placing cart
     */
    void placeOrder(Order order, HttpSession session) throws OutOfStockException, ServiceException;

    /**
     * Change order status in database
     * @param id id of order
     * @param status new status of order
     */
    void changeOrderStatus(Long id, OrderStatus status) throws ServiceException;

    Optional<Order> getById (Long id) throws ServiceException;
}
