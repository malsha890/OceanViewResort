package com.oceanview.controller;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import com.oceanview.util.DBConnection;


@WebServlet({"/staff/dashboard", "/api/dashboard"})
public class StaffDashboardServlet extends HttpServlet {

    Gson gson = new Gson(); 
    // ==============================
    // doGet — handles both UI and API
    // ==============================
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getServletPath();

        
        if ("/api/dashboard".equals(path)) {
            handleAPIGet(request, response);
            return;
        }

       
        try {
            Connection con = DBConnection.getConnection();

            // Total Reservations
            PreparedStatement ps1 = con.prepareStatement("SELECT COUNT(*) FROM reservations");
            ResultSet rs1 = ps1.executeQuery();
            int totalReservations = 0;
            if (rs1.next()) {
                totalReservations = rs1.getInt(1);
            }

            // Total Available Rooms
            PreparedStatement ps2 = con.prepareStatement("SELECT COUNT(*) FROM rooms WHERE status='Available'");
            ResultSet rs2 = ps2.executeQuery();
            int availableRooms = 0;
            if (rs2.next()) {
                availableRooms = rs2.getInt(1);
            }

            // Total Staff
            PreparedStatement ps3 = con.prepareStatement("SELECT COUNT(*) FROM staff WHERE role='staff'");
            ResultSet rs3 = ps3.executeQuery();
            int totalStaff = 0;
            if (rs3.next()) {
                totalStaff = rs3.getInt(1);
            }

            request.setAttribute("totalReservations", totalReservations);
            request.setAttribute("availableRooms", availableRooms);
            request.setAttribute("totalStaff", totalStaff);
            request.getRequestDispatcher("/StaffDashboard.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

 
    private void handleAPIGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        try {
            Connection con = DBConnection.getConnection();

          
            // Total Reservations
            PreparedStatement ps1 = con.prepareStatement("SELECT COUNT(*) FROM reservations");
            ResultSet rs1 = ps1.executeQuery();
            int totalReservations = 0;
            if (rs1.next()) {
                totalReservations = rs1.getInt(1);
            }

            // Total Available Rooms
            PreparedStatement ps2 = con.prepareStatement("SELECT COUNT(*) FROM rooms WHERE status='Available'");
            ResultSet rs2 = ps2.executeQuery();
            int availableRooms = 0;
            if (rs2.next()) {
                availableRooms = rs2.getInt(1);
            }

            // Total Staff
            PreparedStatement ps3 = con.prepareStatement("SELECT COUNT(*) FROM staff WHERE role='staff'");
            ResultSet rs3 = ps3.executeQuery();
            int totalStaff = 0;
            if (rs3.next()) {
                totalStaff = rs3.getInt(1);
            }

            con.close();

            // Build JSON response with all dashboard stats
            Map<String, Object> result = new HashMap<>();
            result.put("status", "success");
            result.put("totalReservations", totalReservations);
            result.put("availableRooms", availableRooms);
            result.put("totalStaff", totalStaff);

            response.setStatus(HttpServletResponse.SC_OK);
            out.write(gson.toJson(result));

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
  
