package com.javamentor.jmp_project.servlet.user;

import com.javamentor.jmp_project.exception.DaoException;
import com.javamentor.jmp_project.exception.InvalidArgumentException;
import com.javamentor.jmp_project.exception.NotFoundException;
import com.javamentor.jmp_project.model.User;
import com.javamentor.jmp_project.service.UserService;
import com.javamentor.jmp_project.service.UserServiceImpl;
import com.javamentor.jmp_project.util.ErrorMessage;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "UserRead", urlPatterns = {"/user", "/user/all", "/user/read", "/user/read/all"})
public class ReadServlet extends HttpServlet {

    private UserService userService = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");

        try {
            if (request.getServletPath().endsWith("all")) {
                List<User> users = userService.getAllUsers();

                if (users != null) {
                    request.setAttribute("users", users);
                    response.setStatus(HttpServletResponse.SC_OK);
                    getServletContext().getRequestDispatcher("/jsp/users.jsp").forward(request, response);
                } else {
                    request.getSession().setAttribute("error", new ErrorMessage("Users not found."));
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    response.sendRedirect("/");
                }
            } else {
                Long id = StringUtils.isNumeric(request.getParameter("id")) ? Long.parseLong(request.getParameter("id")) : null;
                String login = request.getParameter("login");
                User user;

                if (id != null) {
                    user = userService.getUser(id);
                } else if (StringUtils.isNotBlank(login)) {
                    user = userService.getUserByLogin(login);
                } else {
                    request.getSession().setAttribute("error", new ErrorMessage("Invalid user data."));
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.sendRedirect("/");
                    return;
                }

                if (user == null) {
                    request.getSession().setAttribute("error", new ErrorMessage("User not found."));
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    response.sendRedirect("/");
                    return;
                }

                request.setAttribute("user", user);
                response.setStatus(HttpServletResponse.SC_OK);
                getServletContext().getRequestDispatcher("/jsp/user.jsp").forward(request, response);
            }
        } catch (InvalidArgumentException e) {
            request.getSession().setAttribute("error", new ErrorMessage(e.getMessage()));
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.sendRedirect("/");
        } catch (NotFoundException e) {
            request.getSession().setAttribute("error", new ErrorMessage(e.getMessage()));
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.sendRedirect("/");
        } catch (DaoException e) {
            request.getSession().setAttribute("error", new ErrorMessage(e.getMessage()));
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.sendRedirect("/");
        }
    }

}
