package com.example.wt_bookshop.web.exceptions;

/**
 /**
 * @author sasha
 * @version 1.0
 * Exception to all project
 */
public class ProjectException extends Exception{
    private static final long serialVersionUID = 1L;
    private Exception hiddenException;
    public ProjectException(String msg){
        super(msg);
    }
    public ProjectException(String msg, Exception e){
        super(msg);
        hiddenException = e;
    }
    public Exception getHiddenException(){
        return hiddenException;
    }

}
