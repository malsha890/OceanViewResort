package com.oceanview.controller;

import com.oceanview.dao.BillDAO;
import com.oceanview.model.Bill;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.servlet.*;
import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/staff/bills")
public class BillServlet extends HttpServlet {

    BillDAO billDAO = new BillDAO();

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("view".equals(action)) {

            int reservationId = Integer.parseInt(request.getParameter("reservationId"));
            Bill bill = billDAO.getBillByReservation(reservationId);
            
            if (bill == null) {
                billDAO.createBill(reservationId);
                bill = billDAO.getBillByReservation(reservationId);
            }


            request.setAttribute("bill", bill);
            request.getRequestDispatcher("/bill.jsp")
                    .forward(request, response);
        }

        else if ("pay".equals(action)) {

            int billId = Integer.parseInt(request.getParameter("billId"));
            billDAO.markAsPaid(billId);

            response.sendRedirect(request.getContextPath() +
                    "/staff/reservations?action=list");
        }
    }

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws IOException {

        int reservationId = Integer.parseInt(request.getParameter("reservationId"));
        BigDecimal extraCharges = new BigDecimal(request.getParameter("extraCharges"));

        billDAO.createBill(reservationId);

        response.sendRedirect(request.getContextPath() +
                "/staff/bills?action=view&reservationId=" + reservationId);
    }
}