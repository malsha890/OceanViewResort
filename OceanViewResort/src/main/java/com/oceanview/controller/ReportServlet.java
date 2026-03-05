package com.oceanview.controller;

import com.oceanview.dao.ReportDAO;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/admin/reports")
public class ReportServlet extends HttpServlet {

ReportDAO dao = new ReportDAO();

protected void doGet(HttpServletRequest request,
HttpServletResponse response)
throws ServletException, IOException {

request.setAttribute("totalReservations", dao.getTotalReservations());
request.setAttribute("totalRevenue", dao.getTotalRevenue());
request.setAttribute("activeRooms", dao.getActiveRooms());
request.setAttribute("cancelledReservations", dao.getCancelledReservations());

request.setAttribute("monthlyReservations", dao.getMonthlyReservations());
request.setAttribute("monthlyRevenue", dao.getMonthlyRevenue());


request.getRequestDispatcher("/reports.jsp")
.forward(request,response);
}
}
