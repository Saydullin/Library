package com.example.wt_bookshop.service;

import com.example.wt_bookshop.model.entities.cart.Cart;
import com.example.wt_bookshop.model.entities.cart.CartItem;
import com.example.wt_bookshop.model.entities.order.Order;
import com.example.wt_bookshop.model.entities.book.Book;
import com.example.wt_bookshop.model.service.OrderService;
import com.example.wt_bookshop.model.service.impl.OrderServiceImpl;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class OrderServiceImplTest {
    private OrderService orderService;


    @Before
    public void init() {
        orderService = OrderServiceImpl.getInstance();
    }

    @Test
    public void testCreatingOrderFromCart() {
        Cart cart = new Cart();
        cart.setTotalCost(new BigDecimal(100));
        cart.setTotalItems(3);

        List<CartItem> testList = new ArrayList<>();
        testList.add(new CartItem(new Book(), 2));
        cart.setItems(testList);

        Order order = orderService.createOrder(cart);
        Assert.assertEquals(order.getSubtotal(), cart.getTotalCost());
    }

    @Test
    public void testCloningObjectsWhileCreatingOrderFromCart() {
        Cart cart = new Cart();
        cart.setTotalCost(new BigDecimal(100));
        cart.setTotalItems(1);

        List<CartItem> testList = new ArrayList<>();
        testList.add(new CartItem(new Book(), 2));
        cart.setItems(testList);

        Order order = orderService.createOrder(cart);
        for (int i = 0; i < order.getOrderItems().size(); i++) {
            Assert.assertEquals(order.getOrderItems().get(i).getBook(), cart.getItems().get(i).getBook());
            Assert.assertEquals(order.getOrderItems().get(i).getQuantity(), cart.getItems().get(i).getQuantity());
        }
    }

    @After
    public void clean() {
        orderService = null;
    }
}
