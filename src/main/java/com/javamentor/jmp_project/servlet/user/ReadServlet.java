package com.javamentor.jmp_project.servlet.user;

import com.javamentor.jmp_project.model.User;
import com.javamentor.jmp_project.service.UserService;
import com.javamentor.jmp_project.service.UserServiceImpl;
import com.javamentor.jmp_project.util.ErrorMessage;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "UserRead", urlPatterns = "/user")
public class ReadServlet extends HttpServlet {

    private UserService userService = UserServiceImpl.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");

        User user = (User) request.getSession().getAttribute("user");

        if (user == null || user.getClass() != User.class) {
            request.getSession().setAttribute("error", new ErrorMessage("User not authorized."));
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.sendRedirect("/signin");
            return;
        }

        request.setAttribute("_user", user);
        response.setStatus(HttpServletResponse.SC_OK);
        getServletContext().getRequestDispatcher("/jsp/user.jsp").forward(request, response);
    }

}
