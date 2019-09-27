package com.javamentor.jmp_project.servlet.filter;

import com.javamentor.jmp_project.model.User;
import com.javamentor.jmp_project.util.ErrorMessage;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Logger;

@WebFilter(filterName = "UserFilter", urlPatterns = "/user/*")
public class UserFilter implements Filter {

    private static final Logger LOG = Logger.getLogger(UserFilter.class.getName());

    @Override
    public void init(FilterConfig config) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        User user = (User) request.getSession().getAttribute("user");

        LOG.info("User: " + user);

        if (user != null && Arrays.asList("admin", "user").contains(user.getRole())) {
//            request.getSession().setAttribute("note", new NoteMessage("User successful authorized."));
            response.setStatus(HttpServletResponse.SC_ACCEPTED);
            chain.doFilter(req, resp);
        } else {
            LOG.warning("User unauthorized.");
            request.getSession().setAttribute("error", new ErrorMessage("User unauthorized."));
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.sendRedirect("/signin");
        }
    }

    @Override
    public void destroy() {
    }

}
