package com.javamentor.jmp_project.servlet.user;

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

@WebServlet(name = "UserUpdate", urlPatterns = "/user/update")
public class UpdateServlet extends HttpServlet {

    private UserService userService = UserServiceImpl.getInstance();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");

        Long id = StringUtils.isNumeric(request.getParameter("id")) ? Long.parseLong(request.getParameter("id")) : null;
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        String name = request.getParameter("name");
        String email = request.getParameter("email");

        if (id == null || StringUtils.isBlank(login) || StringUtils.isBlank(password)) {
            request.getSession().setAttribute("error", new ErrorMessage("Invalid user data."));
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.sendRedirect("/");
            return;
        }

        User user = new User(id, login, password, name, email);

        try {
            user = userService.updateUser(user);
            request.setAttribute("note", new NoteMessage("User successful updated."));
            response.setStatus(HttpServletResponse.SC_ACCEPTED);
        } catch (InvalidArgumentException e) {
            request.setAttribute("error", new ErrorMessage(e.getMessage()));
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (NotFoundException e) {
            request.setAttribute("error", new ErrorMessage(e.getMessage()));
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } catch (DaoException e) {
            request.setAttribute("error", new ErrorMessage(e.getMessage()));
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }

        request.setAttribute("user", user);
        getServletContext().getRequestDispatcher("/jsp/user.jsp").forward(request, response);
    }

}
