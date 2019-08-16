package com.javamentor.jmp_project.servlet.user;

import com.javamentor.jmp_project.exception.DaoException;
import com.javamentor.jmp_project.exception.IllegalArgumentException;
import com.javamentor.jmp_project.model.User;
import com.javamentor.jmp_project.service.UserService;
import com.javamentor.jmp_project.service.UserServiceImpl;
import com.javamentor.jmp_project.util.AlertMessage;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

@WebServlet(name = "UserRead", urlPatterns = {"/user", "/user/all", "/user/read", "/user/read/all"})
public class ReadServlet extends HttpServlet {

    private static final Logger LOG = Logger.getLogger(ReadServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");

        try (UserService userService = new UserServiceImpl()) {
            if (request.getServletPath().endsWith("all")) {
                List<User> users = userService.getAllUsers();

                if (users != null) {
                    request.setAttribute("users", users);
                    response.setStatus(HttpServletResponse.SC_OK);
                    getServletContext().getRequestDispatcher("/jsp/users.jsp").forward(request, response);
                } else {
                    request.getSession().setAttribute("error", new AlertMessage("Error: users not found."));
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
                    request.getSession().setAttribute("error", new AlertMessage("Error: invalid user data."));
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.sendRedirect("/");
                    return;
                }

                if (user == null) {
                    request.getSession().setAttribute("error", new AlertMessage("Error: user not found."));
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    response.sendRedirect("/");
                    return;
                }

                request.setAttribute("user", user);
                response.setStatus(HttpServletResponse.SC_OK);
                getServletContext().getRequestDispatcher("/jsp/user.jsp").forward(request, response);
            }
        } catch (IllegalArgumentException e) {
            LOG.warning(e.getMessage());
            request.getSession().setAttribute("error", new AlertMessage("Error: invalid user data."));
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.sendRedirect("/");
        } catch (DaoException e) {
            LOG.warning(e.getMessage());
            request.getSession().setAttribute("error", new AlertMessage("Error: user read failed."));
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.sendRedirect("/");
        }
    }

}
