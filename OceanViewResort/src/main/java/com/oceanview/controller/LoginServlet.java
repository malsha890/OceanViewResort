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

        try {
            StaffDAO dao = new StaffDAO();   // create object
            Staff staff = dao.login(email, password);  // call properly

            if (staff != null) {

                HttpSession session = request.getSession();
                session.setAttribute("staff", staff);

                if ("ADMIN".equals(staff.getRole())) {
                    response.sendRedirect("admin/staff");
                } else {
                    response.sendRedirect("StaffDashboard.jsp");
                }

            } else {
                request.setAttribute("error", "Invalid Email or Password!");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();  // Keep this
            request.setAttribute("error", e.getMessage());  // Show real error
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }

    }
}
