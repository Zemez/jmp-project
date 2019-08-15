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
import java.util.logging.Logger;

@WebServlet(name = "UserCreate", urlPatterns = "/user/create")
public class CreateServlet extends HttpServlet {

    private static final Logger LOG = Logger.getLogger(CreateServlet.class.getName());

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");

        String login = request.getParameter("login");
        String password = request.getParameter("password");
        String name = request.getParameter("name");
        String email = request.getParameter("email");

        User user = new User(login, password, name, email);
        request.getSession().setAttribute("user", user);

        if (StringUtils.isBlank(login) || StringUtils.isBlank(password)) {
            request.getSession().setAttribute("error", new AlertMessage("Error: invalid user data."));
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.sendRedirect("/");
            return;
        }

        try (UserService userService = new UserServiceImpl()) {
            user = userService.createUser(user);
            request.setAttribute("user", user);
            request.setAttribute("note", new AlertMessage("Note: user successful added."));
            response.setStatus(HttpServletResponse.SC_OK);
            getServletContext().getRequestDispatcher("/jsp/user.jsp").forward(request, response);
        } catch (IllegalArgumentException e) {
            LOG.warning(e.getMessage());
            request.getSession().setAttribute("error", new AlertMessage("Error: invalid user data."));
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.sendRedirect("/");
        } catch (DaoException e) {
            LOG.warning(e.getMessage());
            request.getSession().setAttribute("error", new AlertMessage("Error: user add failed."));
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.sendRedirect("/");
        }
    }

}
