package com.javamentor.jmp_project.servlet;

import com.javamentor.jmp_project.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/api")
public class ApiServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
//        response.getWriter().println("<h1>Test API</h1>");
        User user = new User("vasya99", "vasya123", "Vasya", "vasya@mail.ru");
        HttpSession session = request.getSession();
        session.setAttribute("user", user);
        response.setStatus(HttpServletResponse.SC_OK);
        getServletContext()
                .getRequestDispatcher("/user.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }
}
