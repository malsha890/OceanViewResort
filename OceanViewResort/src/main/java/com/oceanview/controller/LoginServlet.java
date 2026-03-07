package com.oceanview.controller;

import com.google.gson.Gson;
import com.oceanview.dao.StaffDAO;
import com.oceanview.model.Staff;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

// ✅ EXISTING path kept + new API path added
@WebServlet({"/login", "/api/login"})
public class LoginServlet extends HttpServlet {

    Gson gson = new Gson(); // ✅ NEW: for JSON conversion

    // ==============================
    // doPost — handles both UI and API
    // ==============================
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getServletPath();

        // ✅ NEW: if request comes from /api/login → return JSON token response
        if ("/api/login".equals(path)) {
            handleAPILogin(request, response);
            return;
        }

        // ✅ EXISTING: original UI login logic — completely unchanged
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        try {
            StaffDAO dao = new StaffDAO();
            Staff staff = dao.login(email, password);

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
            e.printStackTrace();
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }

    // ==============================
    // NEW — API Login handler
    // Accepts JSON body → returns
    // staff info + session token
    // ==============================
    private void handleAPILogin(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        try {
            // Read JSON body from request
            // Supports: { "email": "...", "password": "..." }
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = request.getReader().readLine()) != null) {
                sb.append(line);
            }

            // Parse JSON body into a map
            Map<?, ?> body = gson.fromJson(sb.toString(), Map.class);
            String email    = (String) body.get("email");
            String password = (String) body.get("password");

            // ✅ Basic validation — check fields are not empty
            if (email == null || email.isEmpty() ||
                password == null || password.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.write("{\"status\": \"error\", \"message\": \"Email and password are required\"}");
                return;
            }

            // ✅ Reuse your existing DAO login method
            StaffDAO dao = new StaffDAO();
            Staff staff = dao.login(email, password);

            if (staff != null) {
                // ✅ Create session (same as UI login)
                HttpSession session = request.getSession();
                session.setAttribute("staff", staff);

                // Build JSON response with staff info
                Map<String, Object> result = new HashMap<>();
                result.put("status", "success");
                result.put("message", "Login successful");
                result.put("sessionId", session.getId()); // token for API calls
                result.put("staffId", staff.getStaffId());
                result.put("name", staff.getFullName());
                result.put("role", staff.getRole());
                result.put("email", staff.getEmail());

                response.setStatus(HttpServletResponse.SC_OK);
                out.write(gson.toJson(result));

            } else {
                // ✅ Invalid credentials
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                Map<String, String> error = new HashMap<>();
                error.put("status", "error");
                error.put("message", "Invalid Email or Password!");
                out.write(gson.toJson(error));
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            Map<String, String> error = new HashMap<>();
            error.put("status", "error");
            error.put("message", e.getMessage());
            out.write(gson.toJson(error));
        }
    }
}

  
