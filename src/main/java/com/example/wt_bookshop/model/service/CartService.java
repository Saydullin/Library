package com.example.wt_bookshop.model.service;

import com.example.wt_bookshop.model.entities.cart.Cart;
import com.example.wt_bookshop.model.exceptions.OutOfStockException;
import com.example.wt_bookshop.model.exceptions.ServiceException;
import jakarta.servlet.http.HttpSession;

/**
 * @author sasha
 * @version 1.0
 */
public interface CartService {
    /**
     * Get cart from request
     * @param currentSession session with cart
     * @return  cart
     */
    Cart getCart(HttpSession currentSession);

    /**
     *
     * @param cart cart to add
     * @param productId id of product to add
     * @param quantity quantity of product to add
     * @param currentSession session with cart
     * @throws OutOfStockException throws when phone out of stock when adding
     */
    void add(Cart cart, Long productId, int quantity, HttpSession currentSession) throws OutOfStockException, ServiceException;

    /**
     * Update book in cart
     * @param cart cart to update
     * @param productId id of book to update
     * @param quantity quantity of book to update
     * @param currentSession session with cart
     * @throws OutOfStockException throws when phone out of stock when updating
     */

    void update(Cart cart, Long productId, int quantity, HttpSession currentSession) throws OutOfStockException, ServiceException;

    /**
     * Delete book from cart
     * @param cart cart to delete
     * @param productId id of book to delete
     * @param currentSession session with cart
     */

    void delete(Cart cart, Long productId, HttpSession currentSession);

    /**
     * Recalculate cart total price
     * @param cartToRecalculate cat to recalculate
     */

    void reCalculateCart(Cart cartToRecalculate);

    /**
     * Clear cart
     * @param currentSession session with cart
     */
    void clear(HttpSession currentSession);

    /**
     * Remove item from cart
     * @param currentSession session with cart
     * @param phoneId id of book to remove
     */

    void remove(HttpSession currentSession, Long phoneId);

}
