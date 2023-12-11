package com.example.wt_bookshop.web.commands.commandImpl;

import com.example.wt_bookshop.model.dao.BookDao;
import com.example.wt_bookshop.model.dao.impl.JdbcBookDao;
import com.example.wt_bookshop.model.entities.book.Book;
import com.example.wt_bookshop.model.exceptions.DaoException;
import com.example.wt_bookshop.web.JspPageName;
import com.example.wt_bookshop.web.commands.ICommand;
import com.example.wt_bookshop.web.exceptions.CommandException;
import jakarta.servlet.http.HttpServletRequest;

/**
 * @author sasha
 * @version 1.0
 * Command to get product details page
 */
public class ProductDetailsCommand implements ICommand {
    private final BookDao bookDao = JdbcBookDao.getInstance();
    private static final String BOOK_ATTRIBUTE = "book";

    /**
     * Return product details page of current book
     * @param request http request
     * @return product details page jsp path
     * @throws CommandException throws when there is some errors during command execution
     */
    @Override
    public String execute(HttpServletRequest request) throws CommandException {
        Book book;
        try {
            if (request.getParameter("phone_id") == null){
                book = bookDao.get(Long.parseLong(request.getAttribute("phone_id").toString())).orElse(null);
            } else {
                book = bookDao.get(Long.valueOf(request.getParameter("phone_id"))).orElse(null);
            }
        } catch (DaoException e) {
            throw new CommandException(e.getMessage());
        }
        if (book != null) {
            request.setAttribute(BOOK_ATTRIBUTE, book);
            return JspPageName.PRODUCT_PAGE;
        }
        else{
            return JspPageName.PRODUCT_NOT_FOUND_PAGE;
        }
    }
}
