package com.example.wt_bookshop.model.exceptions;

/**
 * @author sasha
 * @version 1.0
 * Exception in layer of services
 */
public class ServiceException extends Exception {
    /**
     * Place message of exception
     *
     * @param message message of exception
     */
    public ServiceException(String message) {
        super(message);
    }
}
