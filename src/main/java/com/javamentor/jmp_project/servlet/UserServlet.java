package com.javamentor.jmp_project.servlet;

import com.javamentor.jmp_project.exception.DaoException;
import com.javamentor.jmp_project.exception.DbException;
import com.javamentor.jmp_project.model.User;
import com.javamentor.jmp_project.service.UserService;
import com.javamentor.jmp_project.util.TemporaryMessage;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

@WebServlet(name="UserServlet", urlPatterns={"/user","/users"})
public class UserServlet extends HttpServlet {

    private static final Logger LOG = Logger.getLogger(UserServlet.class.getName());

    private static final String METHOD_DELETE = "DELETE";
    private static final String METHOD_POST = "POST";
    private static final String METHOD_PUT = "PUT";

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getMethod();
        if (method.equals(METHOD_POST)) {
            String _method = req.getParameter("_method");
            LOG.info("_method: " + _method);
            if (StringUtils.isNotBlank(_method)) {
                _method = _method.toUpperCase();
                if (_method.equals(METHOD_DELETE)) {
                    doDelete(req, resp);
                } else if (_method.equals(METHOD_PUT)) {
                    doPut(req, resp);
                } else {
                    doPost(req, resp);
                }
            } else {
                doPost(req, resp);
            }
        } else {
            super.service(req, resp);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");

        Long id = StringUtils.isNumeric(request.getParameter("id")) ? Long.parseLong(request.getParameter("id")) : 0;
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
                getServletContext().getRequestDispatcher("/jsp/users.jsp").forward(request, response);
                return;
            }
        } catch (DaoException | DbException e) {
            e.printStackTrace();
        }

        if (user == null) {
            request.setAttribute("alert", new TemporaryMessage("Alert: user not found."));
            user = new User();
        }

        session.setAttribute("user", user);
        response.setStatus(HttpServletResponse.SC_OK);
        getServletContext().getRequestDispatcher("/jsp/user.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");

        String login = request.getParameter("login");
        String password = request.getParameter("password");
        String name = request.getParameter("name");
        String email = request.getParameter("email");

        if (StringUtils.isBlank(login) || StringUtils.isBlank(password)) {
            request.getSession().setAttribute("alert", new TemporaryMessage("Alert: invalid user data."));
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.sendRedirect("/");
            return;
        }

        User user;

        try {
            user = new UserService().addUser(login, password, name, email);
            request.setAttribute("message", new TemporaryMessage("Note: user successful added."));
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (DbException | DaoException e) {
            user = new User(login, password, name, email);
            request.setAttribute("alert", new TemporaryMessage("Alert: user not added."));
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }

        request.getSession().setAttribute("user", user);
        getServletContext().getRequestDispatcher("/jsp/user.jsp").forward(request, response);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("alert", new TemporaryMessage("Alert: user not updated."));
        request.getSession().setAttribute("user", new User());
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        getServletContext().getRequestDispatcher("/jsp/user.jsp").forward(request, response);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("alert", new TemporaryMessage("Alert: user not deleted."));
        request.getSession().setAttribute("user", new User());
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        getServletContext().getRequestDispatcher("/jsp/user.jsp").forward(request, response);
    }

}
