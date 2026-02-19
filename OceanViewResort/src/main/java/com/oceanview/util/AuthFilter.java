package com.oceanview.util;

import com.oceanview.model.Staff;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.*;
import java.io.IOException;

@WebFilter("/*")
public class AuthFilter implements Filter {

    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String path = req.getRequestURI();

        // Allow login page and resources
        if (path.endsWith("login.jsp") || path.endsWith("login")) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession session = req.getSession(false);

        if (session == null || session.getAttribute("staff") == null) {
            res.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        Staff staff = (Staff) session.getAttribute("staff");

        // Role Protection
        if (path.contains("adminDashboard") && 
            !staff.getRole().equals("ADMIN")) {
            res.sendRedirect("staffDashboard.jsp");
            return;
        }
        
        chain.doFilter(request, response);
    }
}


