package com.oceanview.controller;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oceanview.dao.ReservationDAO;
import com.oceanview.model.Reservation;
import com.oceanview.util.DBConnection;

@WebServlet("/staff/reservations")
public class ReservationServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        try (Connection conn = DBConnection.getConnection()) {

            ReservationDAO dao = new ReservationDAO(conn);

            if ("search".equals(action)) {

                String keyword = request.getParameter("keyword");
                request.setAttribute("list", dao.search(keyword));

            } else {

                request.setAttribute("list", dao.getAll());
            }

            request.getRequestDispatcher("/reservations.jsp")
                    .forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        try (Connection conn = DBConnection.getConnection()) {

            ReservationDAO dao = new ReservationDAO(conn);

            if ("add".equals(action)) {

                Reservation r = buildReservation(request);
                r.setStaffId((Integer) request.getSession().getAttribute("staffId"));

                dao.addReservation(r);

            } else if ("update".equals(action)) {

                Reservation r = buildReservation(request);
                r.setReservationId(Integer.parseInt(request.getParameter("id")));
                dao.updateReservation(r);

            } else if ("delete".equals(action)) {

                int id = Integer.parseInt(request.getParameter("id"));
                int roomId = Integer.parseInt(request.getParameter("roomId"));
                dao.deleteReservation(id, roomId);
            }

            response.sendRedirect("reservations");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Reservation buildReservation(HttpServletRequest request) throws Exception {

        Reservation r = new Reservation();

        r.setCustomerName(request.getParameter("name"));
        r.setCustomerPhone(request.getParameter("phone"));
        r.setCustomerEmail(request.getParameter("email"));
        r.setRoomId(Integer.parseInt(request.getParameter("roomId")));

        r.setCheckIn(java.sql.Date.valueOf(request.getParameter("checkIn")));
        r.setCheckOut(java.sql.Date.valueOf(request.getParameter("checkOut")));
        r.setTotalAmount(Double.parseDouble(request.getParameter("amount")));
        r.setStatus(request.getParameter("status"));

        return r;
    }
}

