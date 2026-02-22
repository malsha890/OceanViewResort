package com.oceanview.controller;

import com.oceanview.dao.ReservationDAO;
import com.oceanview.model.Reservation;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@WebServlet("/staff/reservations")
public class ReservationServlet extends HttpServlet {

    ReservationDAO reservationDAO = new ReservationDAO();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if (action == null) {
            action = "list";
        }

        switch (action) {

            case "list":
                listReservations(request, response);
                break;

            case "cancel":
                cancelReservation(request, response);
                break;

            case "addForm":
                request.getRequestDispatcher("/reservations.jsp")
                        .forward(request, response);
                break;

            default:
                listReservations(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
throws ServletException, IOException {

String action = request.getParameter("action");

if ("add".equals(action)) {
addReservation(request, response);
} else {
response.sendRedirect(request.getContextPath() + "/staff/reservations?action=list");
}
}
    // ==============================
    // LIST
    // ==============================
    private void listReservations(HttpServletRequest request,
                                  HttpServletResponse response)
            throws ServletException, IOException {

        List<Reservation> list = reservationDAO.getAllReservations();
        request.setAttribute("reservations", list);

        request.getRequestDispatcher("/reservations.jsp")
                .forward(request, response);
    }

    // ==============================
    // ADD
    // ==============================
    private void addReservation(HttpServletRequest request,
                                HttpServletResponse response)
            throws IOException {

        Reservation r = new Reservation();

        r.setCustomerName(request.getParameter("name"));
        r.setCustomerPhone(request.getParameter("phone"));
        r.setCustomerEmail(request.getParameter("email"));
        r.setRoomId(Integer.parseInt(request.getParameter("roomId")));
        r.setCheckIn(LocalDate.parse(request.getParameter("checkIn")));
        r.setCheckOut(LocalDate.parse(request.getParameter("checkOut")));
        r.setRoomType(request.getParameter("roomType"));
        r.setCustomerAddress(request.getParameter("address"));

        reservationDAO.addReservation(r);

        response.sendRedirect(request.getContextPath() + "/staff/reservations?action=list");
    }

    // ==============================
    // CANCEL
    // ==============================
    private void cancelReservation(HttpServletRequest request,
                                   HttpServletResponse response)
            throws IOException {

        int reservationId = Integer.parseInt(request.getParameter("id"));
        int roomId = Integer.parseInt(request.getParameter("roomId"));

        reservationDAO.cancelReservation(reservationId, roomId);

        response.sendRedirect(request.getContextPath() + "/staff/reservations?action=list");
    }
}