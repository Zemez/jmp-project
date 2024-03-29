package com.javamentor.jmp_project.servlet.user;

import com.javamentor.jmp_project.exception.AlreadyExistsException;
import com.javamentor.jmp_project.exception.DaoException;
import com.javamentor.jmp_project.exception.InvalidArgumentException;
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

@WebServlet(name = "UserCreate", urlPatterns = "/user/create")
public class CreateServlet extends HttpServlet {

    private UserService userService = UserServiceImpl.getInstance();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");

        User user = new User(request.getParameter("login"),
                request.getParameter("password"),
                request.getParameter("role"),
                request.getParameter("name"),
                request.getParameter("email"));

        request.getSession().setAttribute("__user", user);

        if (StringUtils.isBlank(user.getLogin()) || StringUtils.isBlank(user.getPassword())) {
            request.getSession().setAttribute("error", new ErrorMessage("Invalid user data."));
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.sendRedirect("/");
            return;
        }

        try {
            user = userService.createUser(user);
            request.getSession().setAttribute("user", user);
            request.getSession().setAttribute("note", new NoteMessage("User successful created."));
            request.setAttribute("_user", user);
            response.setStatus(HttpServletResponse.SC_CREATED);
            response.sendRedirect("/user");
            return;
        } catch (InvalidArgumentException e) {
            request.getSession().setAttribute("error", new ErrorMessage(e.getMessage()));
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (AlreadyExistsException | DaoException e) {
            request.getSession().setAttribute("error", new ErrorMessage(e.getMessage()));
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }

        response.sendRedirect("/");
    }

}
