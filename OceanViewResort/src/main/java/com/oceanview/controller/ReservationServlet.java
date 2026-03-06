package com.oceanview.controller;

import com.google.gson.Gson;
import com.oceanview.dao.ReservationDAO;
import com.oceanview.model.Reservation;
import com.oceanview.util.EmailUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@WebServlet({"/staff/reservations", "/api/reservations"})
public class ReservationServlet extends HttpServlet {

    ReservationDAO reservationDAO = new ReservationDAO();
    Gson gson = new Gson(); 
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getServletPath();

        
        if ("/api/reservations".equals(path)) {
            handleAPIGet(request, response);
            return;
        }

        
        String action = request.getParameter("action");
        if (action == null) { action = "list"; }

        switch (action) {
            case "list":
                listReservations(request, response);
                break;
            case "search":
                searchReservation(request, response);
                break;
            case "cancel":
                cancelReservation(request, response);
                break;
            case "addForm":
                request.getRequestDispatcher("/reservations.jsp").forward(request, response);
                break;
            default:
                listReservations(request, response);
                break;
        }
    }

   
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getServletPath();

        // if request comes from /api/reservations → handle API add
        if ("/api/reservations".equals(path)) {
            handleAPIPost(request, response);
            return;
        }

        
        String action = request.getParameter("action");
        if ("add".equals(action)) {
            addReservation(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/staff/reservations?action=list");
        }
    }

    
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        try {
            int reservationId = Integer.parseInt(request.getParameter("id"));
            int roomId = Integer.parseInt(request.getParameter("roomId"));

            
            reservationDAO.cancelReservation(reservationId, roomId);

            Map<String, String> result = new HashMap<>();
            result.put("status", "success");
            result.put("message", "Reservation cancelled successfully");
            out.write(gson.toJson(result));

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            Map<String, String> error = new HashMap<>();
            error.put("status", "error");
            error.put("message", e.getMessage());
            out.write(gson.toJson(error));
        }
    }

   
    private void handleAPIGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        try {
            String idParam = request.getParameter("id");

            if (idParam != null) {
                // Get single reservation by ID
                Reservation r = reservationDAO.getReservationById(Integer.parseInt(idParam));
                if (r != null) {
                    out.write(gson.toJson(r));
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    out.write("{\"status\": \"error\", \"message\": \"Reservation not found\"}");
                }
            } else {
                // Get all reservations
                List<Reservation> list = reservationDAO.getAllReservations();
                out.write(gson.toJson(list));
            }

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.write("{\"status\": \"error\", \"message\": \"" + e.getMessage() + "\"}");
        }
    }

    
    private void handleAPIPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        try {
            // Read JSON body from request
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = request.getReader().readLine()) != null) {
                sb.append(line);
            }

            // Convert JSON to Reservation object
            Reservation r = gson.fromJson(sb.toString(), Reservation.class);

           
            if (!r.getCheckOut().isAfter(r.getCheckIn())) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.write("{\"status\": \"error\", \"message\": \"Check-out must be after check-in date.\"}");
                return;
            }

            
            boolean available = reservationDAO.isRoomAvailable(
                    r.getRoomId(), r.getCheckIn(), r.getCheckOut());

            if (!available) {
                response.setStatus(HttpServletResponse.SC_CONFLICT);
                out.write("{\"status\": \"error\", \"message\": \"Room is already booked for selected dates.\"}");
                return;
            }

            
            reservationDAO.addReservation(r);
            EmailUtil.sendReservationEmail(
                    r.getCustomerEmail(),
                    r.getCustomerName(),
                    r.getRoomType(),
                    r.getCheckIn().toString(),
                    r.getCheckOut().toString()
            );

            response.setStatus(HttpServletResponse.SC_CREATED);
            out.write("{\"status\": \"success\", \"message\": \"Reservation added successfully\"}");

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.write("{\"status\": \"error\", \"message\": \"" + e.getMessage() + "\"}");
        }
    }

 
    private void listReservations(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Reservation> list = reservationDAO.getAllReservations();
        request.setAttribute("reservations", list);
        request.getRequestDispatcher("/reservations.jsp").forward(request, response);
    }

    
    private void addReservation(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        try {
            String name = request.getParameter("name");
            String phone = request.getParameter("phone");
            String email = request.getParameter("email");
            int roomId = Integer.parseInt(request.getParameter("roomId"));
            LocalDate checkIn = LocalDate.parse(request.getParameter("checkIn"));
            LocalDate checkOut = LocalDate.parse(request.getParameter("checkOut"));
            String roomType = request.getParameter("roomType");
            String address = request.getParameter("address");

            //  STEP 2A — DATE VALIDATION
            if (!checkOut.isAfter(checkIn)) {
                request.setAttribute("error", "Check-out must be after check-in date.");
                listReservations(request, response);
                return;
            }

            //  STEP 2B — OVERLAP VALIDATION
            boolean available = reservationDAO.isRoomAvailable(roomId, checkIn, checkOut);
            if (!available) {
                request.setAttribute("error", "This room is already booked for selected dates.");
                listReservations(request, response);
                return;
            }

            // STEP 2C — SAVE IF VALID
            Reservation r = new Reservation();
            r.setCustomerName(name);
            r.setCustomerPhone(phone);
            r.setCustomerEmail(email);
            r.setRoomId(roomId);
            r.setCheckIn(checkIn);
            r.setCheckOut(checkOut);
            r.setRoomType(roomType);
            r.setCustomerAddress(address);

            reservationDAO.addReservation(r);
            EmailUtil.sendReservationEmail(
                    r.getCustomerEmail(),
                    r.getCustomerName(),
                    r.getRoomType(),
                    r.getCheckIn().toString(),
                    r.getCheckOut().toString()
            );

            response.sendRedirect(request.getContextPath() + "/staff/reservations?action=list");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", e.getMessage());
            listReservations(request, response);
        }
    }

 
    private void cancelReservation(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int reservationId = Integer.parseInt(request.getParameter("id"));
        int roomId = Integer.parseInt(request.getParameter("roomId"));
        reservationDAO.cancelReservation(reservationId, roomId);
        response.sendRedirect(request.getContextPath() + "/staff/reservations?action=list");
    }
    
    private void searchReservation(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {

            String keyword = request.getParameter("keyword");

            List<Reservation> list = reservationDAO.searchReservation(keyword);

            request.setAttribute("reservations", list);

            request.getRequestDispatcher("/reservations.jsp")
                   .forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}