package com.example.wt_bookshop.web.commands.commandImpl;

import com.example.wt_bookshop.web.JspPageName;
import com.example.wt_bookshop.web.commands.ICommand;
import com.example.wt_bookshop.web.exceptions.CommandException;
import jakarta.servlet.http.HttpServletRequest;

/**
 * @author sasha
 * @version 1.0
 * Command to unknown name
 */
public class UnknownCommand implements ICommand {
    /**
     * Return error page
     *
     * @param request http request
     * @return return error page jsp path
     * @throws CommandException throws when there is some errors during command execution
     */
    @Override
    public String execute(HttpServletRequest request) throws CommandException {
        return JspPageName.ERROR_PAGE;
    }
}
