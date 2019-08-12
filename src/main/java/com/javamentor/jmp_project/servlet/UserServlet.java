package com.javamentor.jmp_project.servlet;

import com.javamentor.jmp_project.exception.DaoException;
import com.javamentor.jmp_project.exception.DbException;
import com.javamentor.jmp_project.model.User;
import com.javamentor.jmp_project.service.UserService;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet(name="UserServlet", urlPatterns={"/user","/users"})
public class UserServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");

        Long id = request.getParameter("id") == null ? 0 : Long.parseLong(request.getParameter("id"));
        String login = request.getParameter("login");

        HttpSession session = request.getSession();
        UserService userService = new UserService();
        User user = null;

        try {
            if (id > 0) {
                user = userService.getUser(id);
            } else if (StringUtils.isNotBlank(login)) {
                user = userService.getUserBy("login", login);
            } else {
                List<User> users = userService.getAllUsers();

                session.setAttribute("users", users);
                response.setStatus(HttpServletResponse.SC_OK);
                getServletContext().getRequestDispatcher("/users.jsp").forward(request, response);
                return;
            }
        } catch (DaoException | DbException e) {
            e.printStackTrace();
        }

        if (user == null) user = new User();

        session.setAttribute("user", user);
        response.setStatus(HttpServletResponse.SC_OK);
        getServletContext().getRequestDispatcher("/user.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

}
