package com.example.wt_bookshop.model.service.impl;

import com.example.wt_bookshop.model.dao.BookDao;
import com.example.wt_bookshop.model.dao.StockDao;
import com.example.wt_bookshop.model.dao.impl.JdbcBookDao;
import com.example.wt_bookshop.model.dao.impl.JdbcStockDao;
import com.example.wt_bookshop.model.entities.cart.Cart;
import com.example.wt_bookshop.model.entities.cart.CartItem;
import com.example.wt_bookshop.model.entities.book.Book;
import com.example.wt_bookshop.model.exceptions.DaoException;
import com.example.wt_bookshop.model.exceptions.OutOfStockException;
import com.example.wt_bookshop.model.exceptions.ServiceException;
import com.example.wt_bookshop.model.service.CartService;
import jakarta.servlet.http.HttpSession;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Service to work with cart
 *
 * @author sasha
 * @version 1.0
 */
public class HttpSessionCartService implements CartService {
    /**
     * Attribute of cart in session
     */
    private static final String CART_SESSION_ATTRIBUTE = HttpSessionCartService.class.getName() + ".cart";
    /**
     * Attribute of cart in request attribute
     */
    private static final String CART_ATTRIBUTE = "cart";
    /**
     * Instance of HttpSessionCartService
     */
    private static volatile HttpSessionCartService instance;
    /**
     * Instance of BookDao
     */
    private BookDao bookDao;
    /**
     * Instance of StockDao
     */
    private StockDao stockDao;

    /**
     * Realisation of Singleton pattern
     *
     * @return instance of HttpSessionCartServiece
     */

    public static HttpSessionCartService getInstance() {
        if (instance == null) {
            synchronized (HttpSessionCartService.class) {
                if (instance == null) {
                    instance = new HttpSessionCartService();
                }
            }
        }
        return instance;
    }

    /**
     * Constructor of HttpSessionCartService
     */
    private HttpSessionCartService() {
        bookDao = JdbcBookDao.getInstance();
        stockDao = JdbcStockDao.getInstance();
    }

    /**
     * Get cart from session
     *
     * @param currentSession session with cart
     * @return cart from session
     */
    @Override
    public Cart getCart(HttpSession currentSession) {
        synchronized (currentSession) {
            Cart cart = (Cart) currentSession.getAttribute(CART_SESSION_ATTRIBUTE);
            if (cart == null) {
                cart = new Cart();
                currentSession.setAttribute(CART_SESSION_ATTRIBUTE, cart);
            }
            if (cart.getTotalCost() == null) {
                cart.setTotalCost(BigDecimal.ZERO);
            }
            return cart;
        }
    }

    /**
     * Add Phone to cart
     *
     * @param cart           cart to adding
     * @param productId      productId of book to add
     * @param quantity       quantity of book to add
     * @param currentSession session with cart
     * @throws OutOfStockException throws when phone outOfStock
     * @throws ServiceException    throws when there is some errors during service method execution
     */
    @Override
    public void add(Cart cart, Long productId, int quantity, HttpSession currentSession) throws OutOfStockException, ServiceException {
        Optional<CartItem> productMatch;
        synchronized (currentSession) {
            Book book;
            try {
                book = bookDao.get(productId).orElse(null);
            } catch (DaoException e) {
                throw new ServiceException(e.getMessage());
            }
            if (book != null) {
                if (countingQuantityIncludingCart(cart, book) < quantity) {
                    throw new OutOfStockException(book, quantity, countingQuantityIncludingCart(cart, book));
                }
                if ((productMatch = getCartItemMatch(cart, book)).isPresent()) {
                    cart.getItems().
                            get(cart.getItems().indexOf(productMatch.get())).
                            setQuantity(productMatch.get().getQuantity() + quantity);
                } else {
                    cart.getItems().add(new CartItem(book, quantity));
                    currentSession.setAttribute(CART_ATTRIBUTE, cart);
                }
                reCalculateCart(cart);
            }
        }
    }

    /**
     * Calculate quantity of book with cart
     *
     * @param cart  cart with phones to recalculate
     * @param book book to recalculate
     * @return available quantity of phone minus quantity of phone in cart
     */
    private int countingQuantityIncludingCart(Cart cart, Book book) throws ServiceException {
        int result = 0;
        try {
            result = stockDao.availableStock(book.getId());
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage());
        }
        Integer quantityInCart = cart.getItems().stream()
                .filter(currProduct -> currProduct.getBook().equals(book))
                .map(CartItem::getQuantity)
                .findFirst()
                .orElse(0);
        result -= quantityInCart;
        return result;
    }

    /**
     * Update quantity of book in cart
     *
     * @param cart           cart to update
     * @param productId      id of book to update
     * @param quantity       quantity of book to update
     * @param currentSession session with cart
     * @throws OutOfStockException throws when book quantity out of stock during updating
     * @throws ServiceException    throws when there is some errors during service method execution
     */
    @Override
    public void update(Cart cart, Long productId, int quantity, HttpSession currentSession) throws OutOfStockException, ServiceException {
        synchronized (currentSession) {
            Book book;
            try {
                book = bookDao.get(productId).orElse(null);
            } catch (DaoException e) {
                throw new ServiceException(e.getMessage());
            }
            if (book != null) {
                int availableStock = 0;
                try {
                    availableStock = stockDao.availableStock(book.getId());
                } catch (DaoException e) {
                    throw new ServiceException(e.getMessage());
                }
                if (quantity > availableStock) {
                    throw new OutOfStockException(book, quantity, availableStock);
                }
                getCartItemMatch(cart, book).ifPresent(cartItem -> cart.getItems().
                        get(cart.getItems().indexOf(cartItem)).
                        setQuantity(quantity));
                reCalculateCart(cart);
            }
        }
    }

    /**
     * Delete item from cart
     *
     * @param cart           cart to delete
     * @param productId      id of book to delete
     * @param currentSession session with cart
     */
    @Override
    public void delete(Cart cart, Long productId, HttpSession currentSession) {
        synchronized (currentSession) {
            cart.getItems().removeIf(item -> productId.equals(item.getBook().getId()));
            reCalculateCart(cart);
        }
    }

    /**
     * Recalculate cart
     *
     * @param cartToRecalculate cat to recalculate
     */
    @Override
    public void reCalculateCart(Cart cartToRecalculate) {
        BigDecimal totalCost = BigDecimal.ZERO;
        cartToRecalculate.setTotalItems(
                cartToRecalculate.getItems().stream().
                        map(CartItem::getQuantity).
                        mapToInt(q -> q).
                        sum()
        );
        for (CartItem item : cartToRecalculate.getItems()) {
            totalCost = totalCost.add(
                    item.getBook().getPrice().
                            multiply(BigDecimal.valueOf(item.getQuantity())));
        }
        cartToRecalculate.setTotalCost(totalCost);
    }

    /**
     * Find cart item in cart
     *
     * @param cart    cart in witch we find
     * @param product product to find
     * @return cartItem
     */
    private Optional<CartItem> getCartItemMatch(Cart cart, Book product) {
        return cart.getItems().stream().
                filter(currProduct -> currProduct.getBook().getId().equals(product.getId())).
                findAny();
    }

    /**
     * Clear cart in request
     *
     * @param currentSession session with cart
     */
    @Override
    public void clear(HttpSession currentSession) {
        Cart cart = getCart(currentSession);
        cart.getItems().clear();
        reCalculateCart(cart);
    }

    /**
     * Remove item from cart
     *
     * @param currentSession session with cart
     * @param phoneId        id of book to remove
     */
    @Override
    public void remove(HttpSession currentSession, Long phoneId) {
        Cart cart = getCart(currentSession);
        cart.getItems().removeIf(item -> phoneId.equals(item.getBook().getId()));
        reCalculateCart(cart);
    }
}
