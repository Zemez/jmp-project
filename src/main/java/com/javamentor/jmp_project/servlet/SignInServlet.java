package com.javamentor.jmp_project.servlet;

import com.javamentor.jmp_project.exception.DaoException;
import com.javamentor.jmp_project.exception.InvalidArgumentException;
import com.javamentor.jmp_project.exception.NotFoundException;
import com.javamentor.jmp_project.model.User;
import com.javamentor.jmp_project.service.UserService;
import com.javamentor.jmp_project.service.UserServiceImpl;
import com.javamentor.jmp_project.util.ErrorMessage;
import com.javamentor.jmp_project.util.NoteMessage;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

@WebServlet(name = "SignIn", urlPatterns = "/signin")
public class SignInServlet extends HttpServlet {

    private static final Logger LOG = Logger.getLogger(SignInServlet.class.getName());

    private UserService userService = UserServiceImpl.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        getServletContext().getRequestDispatcher("/jsp/signin.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");

        String login = request.getParameter("login");
        String password = request.getParameter("password");

        if (StringUtils.isBlank(login) || StringUtils.isBlank(password)) {
            request.getSession().setAttribute("error", new ErrorMessage("Invalid user data."));
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.sendRedirect("/signin");
            return;
        }

        request.getSession().setAttribute("_login", login);

        User user = null;

        try {
            user = userService.getUserByLogin(login);
        } catch (DaoException | InvalidArgumentException | NotFoundException e) {
            LOG.warning("User sign in failed: " + e.getMessage());
        }

        LOG.info("User: " + user);

        if (user != null && user.getLogin().equals(login)) {
            LOG.info("User successful logged in.");
            request.getSession().setAttribute("user", user);
            request.getSession().setAttribute("note", new NoteMessage("User successful logged in."));
            response.setStatus(HttpServletResponse.SC_OK);
            response.sendRedirect("/user");
        } else {
            LOG.warning("Invalid user login or password.");
            request.getSession().setAttribute("error", new ErrorMessage("Invalid user login or password."));
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.sendRedirect("/signin");
        }
    }

}
