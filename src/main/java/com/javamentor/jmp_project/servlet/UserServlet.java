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
        resp.setContentType("text/html;charset=utf-8");
        String method = req.getMethod();
        LOG.info("method: " + method);

//        Enumeration<String> el = req.getParameterNames();
//        while (el.hasMoreElements()) LOG.info(el.nextElement());

        if (method.equals(METHOD_POST)) {
            String _method = req.getParameter("_method");
            LOG.info("_method: " + _method);
            if (StringUtils.isNotBlank(_method)) {
                _method = _method.toUpperCase();
                switch (_method) {
                    case METHOD_DELETE:
                        doDelete(req, resp);
                        break;
                    case METHOD_PUT:
                        doPut(req, resp);
                        break;
                    default:
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
        Long id = StringUtils.isNumeric(request.getParameter("id")) ? Long.parseLong(request.getParameter("id")) : 0L;
        String login = request.getParameter("login");

        User user = null;

        try (UserService userService = new UserService()) {
            if (id > 0) {
                user = userService.getUser(id);
            } else if (StringUtils.isNotBlank(login)) {
                user = userService.getUserBy("login", login);
            } else {
                List<User> users = userService.getAllUsers();

                request.setAttribute("users", users);
                response.setStatus(HttpServletResponse.SC_OK);
                getServletContext().getRequestDispatcher("/jsp/users.jsp").forward(request, response);
                return;
            }
        } catch (DaoException | DbException e) {
            e.printStackTrace();
        }

        if (user == null) {
            request.getSession().setAttribute("alert", new TemporaryMessage("Alert: user not found."));
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.sendRedirect("/");
            return;
        }

        response.setStatus(HttpServletResponse.SC_OK);
        request.setAttribute("user", user);
        getServletContext().getRequestDispatcher("/jsp/user.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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

        try (UserService userService = new UserService()) {
            user = userService.addUser(login, password, name, email);
            request.setAttribute("message", new TemporaryMessage("Note: user successful added."));
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (DbException | DaoException e) {
            user = new User(login, password, name, email);
            request.setAttribute("alert", new TemporaryMessage("Alert: user not added."));
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }

        request.setAttribute("user", user);
        getServletContext().getRequestDispatcher("/jsp/user.jsp").forward(request, response);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long id = StringUtils.isNumeric(request.getParameter("id")) ? Long.parseLong(request.getParameter("id")) : 0L;
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

        try (UserService userService = new UserService()) {
            user = userService.updateUser(id, login, password, name, email);
            request.setAttribute("message", new TemporaryMessage("Note: user successful updated."));
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (DbException | DaoException e) {
            user = new User(id, login, password, name, email);
            request.setAttribute("alert", new TemporaryMessage("Alert: user not updated."));
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }

        request.setAttribute("user", user);
        getServletContext().getRequestDispatcher("/jsp/user.jsp").forward(request, response);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long id = StringUtils.isNumeric(request.getParameter("id")) ? Long.parseLong(request.getParameter("id")) : 0L;
        String login = request.getParameter("login");

        if (id <= 0 && StringUtils.isBlank(login)) {
            request.getSession().setAttribute("alert", new TemporaryMessage("Alert: invalid user data."));
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.sendRedirect("/");
            return;
        }

        try (UserService userService = new UserService()) {
            if (id <= 0) {
                User user = userService.getUserBy("login", login);
                id = user == null ? 0L : user.getId();
            }

            if (id > 0) {
                userService.deleteUser(id);
                request.getSession().setAttribute("message", new TemporaryMessage("Note: user successful deleted."));
                response.setStatus(HttpServletResponse.SC_ACCEPTED);
            } else {
                request.getSession().setAttribute("alert", new TemporaryMessage("Alert: user not found."));
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        } catch (DbException | DaoException e) {
            request.getSession().setAttribute("alert", new TemporaryMessage("Alert: user not deleted."));
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }
        response.sendRedirect("/");
    }

}
