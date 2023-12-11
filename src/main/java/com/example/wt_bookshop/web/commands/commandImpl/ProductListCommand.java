package com.example.wt_bookshop.web.commands.commandImpl;

import com.example.wt_bookshop.model.dao.BookDao;
import com.example.wt_bookshop.model.dao.impl.JdbcBookDao;
import com.example.wt_bookshop.model.enums.SortField;
import com.example.wt_bookshop.model.enums.SortOrder;
import com.example.wt_bookshop.model.exceptions.DaoException;
import com.example.wt_bookshop.web.JspPageName;
import com.example.wt_bookshop.web.commands.ICommand;
import com.example.wt_bookshop.web.exceptions.CommandException;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Optional;

/**
 * @author sasha
 * @version 1.0
 * Command to show list of products
 */
public class ProductListCommand implements ICommand {
    private final BookDao bookDao = JdbcBookDao.getInstance();
    private static final String QUERY_PARAMETER = "query";
    private static final String SORT_PARAMETER = "sort";
    private static final String ORDER_PARAMETER = "order";
    private static final String BOOK_ATTRIBUTE = "books";
    private static final String PAGE_PARAMETER = "page";
    private static final String PAGE_ATTRIBUTE = "numberOfPages";
    private static final int PHONES_ON_PAGE = 10;

    /**
     * Get product list and return product list jsp
     * @param request http request
     * @return product list jsp path
     * @throws CommandException throws when there is some errors during command execution
     */
    @Override
    public String execute(HttpServletRequest request) throws CommandException {
        String pageNumber = request.getParameter(PAGE_PARAMETER);
        Long number;
        try {
            request.setAttribute(BOOK_ATTRIBUTE, bookDao.findAll(((pageNumber == null ? 1 : Integer.parseInt(pageNumber)) - 1) * PHONES_ON_PAGE, PHONES_ON_PAGE,
                    Optional.ofNullable(request.getParameter(SORT_PARAMETER)).map(SortField::valueOf).orElse(null),
                    Optional.ofNullable(request.getParameter(ORDER_PARAMETER)).map(SortOrder::valueOf).orElse(null), request.getParameter(QUERY_PARAMETER)));
            number = bookDao.numberByQuery(request.getParameter(QUERY_PARAMETER));
        } catch (DaoException e) {
            throw new CommandException(e.getMessage());
        }
        request.setAttribute(PAGE_ATTRIBUTE, (number + PHONES_ON_PAGE - 1) / PHONES_ON_PAGE);
        return JspPageName.PRODUCT_LIST_JSP;
    }
}
