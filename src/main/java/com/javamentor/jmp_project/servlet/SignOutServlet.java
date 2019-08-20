package com.javamentor.jmp_project.servlet;

import com.javamentor.jmp_project.model.User;
import com.javamentor.jmp_project.util.NoteMessage;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "SignOut", urlPatterns = {"/signout"})
public class SignOutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        User user = (User) request.getSession().getAttribute("user");
        if (user != null) {
            request.getSession().removeAttribute("user");
            request.getSession(true);
            request.getSession().setAttribute("note", new NoteMessage("User successful logged out."));
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            request.getSession().setAttribute("error", new NoteMessage("User not logged in."));
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        response.sendRedirect("/");
    }

}
