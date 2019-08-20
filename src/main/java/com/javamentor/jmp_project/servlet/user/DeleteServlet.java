package com.javamentor.jmp_project.servlet.user;

import com.javamentor.jmp_project.exception.DaoException;
import com.javamentor.jmp_project.exception.InvalidArgumentException;
import com.javamentor.jmp_project.exception.NotFoundException;
import com.javamentor.jmp_project.model.User;
import com.javamentor.jmp_project.service.UserService;
import com.javamentor.jmp_project.service.UserServiceImpl;
import com.javamentor.jmp_project.util.ErrorMessage;
import com.javamentor.jmp_project.util.NoteMessage;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "UserDelete", urlPatterns = "/user/delete")
public class DeleteServlet extends HttpServlet {

    private UserService userService = UserServiceImpl.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = (User) request.getSession().getAttribute("user");

        if (user == null || user.getId() == null) {
            request.getSession().setAttribute("error", new ErrorMessage("Invalid user data."));
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } else {
            try {
                userService.deleteUser(user.getId());
                request.getSession().removeAttribute("user");
                request.getSession().setAttribute("note", new NoteMessage("User successful deleted."));
                response.setStatus(HttpServletResponse.SC_ACCEPTED);
            } catch (InvalidArgumentException e) {
                request.getSession().setAttribute("error", new ErrorMessage(e.getMessage()));
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            } catch (NotFoundException e) {
                request.getSession().setAttribute("error", new ErrorMessage(e.getMessage()));
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            } catch (DaoException e) {
                request.getSession().setAttribute("error", new ErrorMessage(e.getMessage()));
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            }
        }
        response.sendRedirect("/");
    }

}
