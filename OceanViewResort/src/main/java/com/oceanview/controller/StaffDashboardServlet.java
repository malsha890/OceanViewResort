package com.oceanview.controller;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import com.oceanview.util.DBConnection;
@WebServlet("/staff/dashboard")
public class StaffDashboardServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Connection con = DBConnection.getConnection();
            // Total Reservations
            PreparedStatement ps1 = con.prepareStatement("SELECT COUNT(*) FROM reservations");
            ResultSet rs1 = ps1.executeQuery();
            int totalReservations = 0;
            if(rs1.next()){
                totalReservations = rs1.getInt(1);
            }
            // Total Available Rooms
            PreparedStatement ps2 = con.prepareStatement("SELECT COUNT(*) FROM rooms WHERE status='Available'");
            ResultSet rs2 = ps2.executeQuery();
            int availableRooms = 0;
            if(rs2.next()){
                availableRooms = rs2.getInt(1);
            }
            // Total Staff
            PreparedStatement ps3 = con.prepareStatement("SELECT COUNT(*) FROM staff WHERE role='staff'");
            ResultSet rs3 = ps3.executeQuery();
            int totalStaff = 0;
            if(rs3.next()){
                totalStaff = rs3.getInt(1);
            }
            request.setAttribute("totalReservations", totalReservations);
            request.setAttribute("availableRooms", availableRooms);
            request.setAttribute("totalStaff", totalStaff);
            request.getRequestDispatcher("/StaffDashboard.jsp").forward(request, response);
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}can you add it this too.
  
