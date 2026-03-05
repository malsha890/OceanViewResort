package com.oceanview.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.oceanview.dao.StaffDAO;
import com.oceanview.model.Staff;
import com.oceanview.util.PasswordUtil;

@WebServlet("/admin/staff")
public class AdminStaffServlet extends HttpServlet {

    private StaffDAO staffDAO;

    public void init() {
        try {
            staffDAO = new StaffDAO();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String search = request.getParameter("search");
        String editId = request.getParameter("edit");

        try {

            if (editId != null) {
                Staff staff = staffDAO.getStaffById(Integer.parseInt(editId));
                request.setAttribute("editStaff", staff);
            }

            List<Staff> staffList;

            if (search != null && !search.isEmpty()) {
                staffList = staffDAO.searchStaff(search.trim());
            } else {
                staffList = staffDAO.getAllStaff();
            }

            request.setAttribute("staffList", staffList);
            request.getRequestDispatcher("/staffManagement.jsp")
                   .forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/staffManagement.jsp")
                   .forward(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        try {

            if ("add".equals(action)) {

                String fullName = request.getParameter("fullName");
                String email = request.getParameter("email");
                String rawPassword = request.getParameter("password");
                String role = request.getParameter("role");

                //  PASSWORD VALIDATION HERE
                if (!rawPassword.matches("(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[^A-Za-z0-9]).{8,}")) {

                    request.setAttribute("error",
                            "Password must contain at least 8 characters, including uppercase, lowercase, number and special character.");

                    // reload staff list before forwarding
                    List<Staff> staffList = staffDAO.getAllStaff();
                    request.setAttribute("staffList", staffList);

                    request.getRequestDispatcher("/staffManagement.jsp")
                           .forward(request, response);
                    return;
                }

                // If password valid → hash it
                String hashedPassword = PasswordUtil.hashPassword(rawPassword);

                Staff staff = new Staff();
                staff.setFullName(fullName);
                staff.setEmail(email);
                staff.setPassword(hashedPassword);
                staff.setRole(role);

                staffDAO.addStaff(staff);

            } else if ("update".equals(action)) {

                Staff staff = new Staff();
                staff.setStaffId(Integer.parseInt(request.getParameter("id")));
                staff.setFullName(request.getParameter("fullName"));
                staff.setEmail(request.getParameter("email"));
                staff.setRole(request.getParameter("role"));

                staffDAO.updateStaff(staff);

            } else if ("delete".equals(action)) {

                int id = Integer.parseInt(request.getParameter("id"));
                staffDAO.deleteStaff(id);
            }

            response.sendRedirect(request.getContextPath() + "/admin/staff");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", e.getMessage());
            doGet(request, response);
        }
    }
}