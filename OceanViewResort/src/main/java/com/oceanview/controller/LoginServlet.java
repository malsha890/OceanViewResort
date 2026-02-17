package com.oceanview.controller;

import com.oceanview.dao.StaffDAO;
import com.oceanview.model.Staff;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        StaffDAO staffDAO = new StaffDAO();
        Staff staff = staffDAO.login(email, password);

        if (staff != null) {

            HttpSession session = request.getSession();
            session.setAttribute("staff", staff);

            // ROLE BASED REDIRECT
            if (staff.getRole().equals("ADMIN")) {
                response.sendRedirect("AdminDashboard.jsp");
            } else {
                response.sendRedirect("StaffDashboard.jsp");
            }

        } else {
            request.setAttribute("error", "Invalid Email or Password!");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}

