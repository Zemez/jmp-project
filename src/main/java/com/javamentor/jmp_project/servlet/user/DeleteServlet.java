package com.javamentor.jmp_project.servlet.user;

import com.javamentor.jmp_project.exception.DaoException;
import com.javamentor.jmp_project.model.User;
import com.javamentor.jmp_project.service.UserServiceImpl;
import com.javamentor.jmp_project.util.AlertMessage;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

@WebServlet(name = "UserDelete", urlPatterns = "/user/delete")
public class DeleteServlet extends HttpServlet {

    private static final Logger LOG = Logger.getLogger(DeleteServlet.class.getName());

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Long id = StringUtils.isNumeric(request.getParameter("id")) ? Long.parseLong(request.getParameter("id")) : null;
        String login = request.getParameter("login");

        try (UserServiceImpl userService = new UserServiceImpl()) {
            if (id == null || id < 1) {
                if (StringUtils.isNotBlank(login)) {
                    try {
                        User user = userService.getUserByLogin(login);
                        id = user.getId();
                    } catch (DaoException e) {
                        LOG.warning(e.getMessage());
                        request.getSession().setAttribute("error", new AlertMessage("Error: user not found."));
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    }
                } else {
                    request.getSession().setAttribute("error", new AlertMessage("Error: invalid user data."));
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                }
            }

            if (id != null && id > 0) {
                userService.deleteUser(id);
                request.getSession().setAttribute("note", new AlertMessage("Note: user successful deleted."));
                response.setStatus(HttpServletResponse.SC_ACCEPTED);
            }
        } catch (DaoException e) {
            LOG.warning(e.getMessage());
            request.getSession().setAttribute("error", new AlertMessage("Error: user delete failed."));
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }
        response.sendRedirect("/");
    }

}
