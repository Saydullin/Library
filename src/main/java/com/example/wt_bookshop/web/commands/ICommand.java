package com.example.wt_bookshop.web.commands;

import com.example.wt_bookshop.web.exceptions.CommandException;
import jakarta.servlet.http.HttpServletRequest;

/**
 * @author sasha
 * @version 1.0
 * Interface of all commands
 */
public interface ICommand {
    /**
     * Main function of all commands
     *
     * @param request http request
     * @return jsp path
     * @throws CommandException throws when some errors during execution of command
     */
    String execute(HttpServletRequest request) throws CommandException;
}
