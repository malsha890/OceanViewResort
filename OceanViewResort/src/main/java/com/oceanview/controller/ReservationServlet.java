package com.oceanview.controller; 
import com.oceanview.dao.ReservationDAO;
 
import com.oceanview.model.Reservation;
import com.oceanview.util.EmailUtil;

import javax.servlet.ServletException; 
import javax.servlet.annotation.WebServlet; 
import javax.servlet.http.*; 
import java.io.IOException; 
import java.time.LocalDate; 
import java.util.List; 

@WebServlet("/staff/reservations") 
public class ReservationServlet extends HttpServlet { ReservationDAO reservationDAO = new ReservationDAO(); 
@Override protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { String action = request.getParameter("action"); 
if (action == null) { action = "list"; }
switch (action) { case "list": listReservations(request, response); 
break; case "cancel": cancelReservation(request, response); 
break; case "addForm": request.getRequestDispatcher("/reservations.jsp") .forward(request, response); 
break; default: listReservations(request, response); break; } } 
@Override protected void doPost
(HttpServletRequest request, HttpServletResponse response) 
		throws ServletException, IOException { String action = request.getParameter("action");
		if ("add".equals(action)) { addReservation(request, response); } 
		else { response.sendRedirect(request.getContextPath() + "/staff/reservations?action=list"); } } 
// ============================== // LIST // ============================== 
private void listReservations(HttpServletRequest request, 
		HttpServletResponse response) throws ServletException,
IOException { List<Reservation> list = reservationDAO.getAllReservations(); 
request.setAttribute("reservations", list);
request.getRequestDispatcher("/reservations.jsp") .forward(request, response); }
// ============================== // ADD // ============================== 
private void addReservation(HttpServletRequest request,
        HttpServletResponse response)
throws IOException, ServletException {

try {

String name = request.getParameter("name");
String phone = request.getParameter("phone");
String email = request.getParameter("email");
int roomId = Integer.parseInt(request.getParameter("roomId"));

LocalDate checkIn =
LocalDate.parse(request.getParameter("checkIn"));
LocalDate checkOut =
LocalDate.parse(request.getParameter("checkOut"));

String roomType = request.getParameter("roomType");
String address = request.getParameter("address");

// ✅ STEP 2A — DATE VALIDATION
if (!checkOut.isAfter(checkIn)) {

request.setAttribute("error",
"Check-out must be after check-in date.");

listReservations(request, response);
return;
}

// ✅ STEP 2B — OVERLAP VALIDATION
boolean available =
reservationDAO.isRoomAvailable(roomId, checkIn, checkOut);

if (!available) {

request.setAttribute("error",
"This room is already booked for selected dates.");

listReservations(request, response);
return;
}

// ✅ STEP 2C — SAVE IF VALID
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
response.sendRedirect(request.getContextPath()
+ "/staff/reservations?action=list");

} catch (Exception e) {
e.printStackTrace();
request.setAttribute("error", e.getMessage());
listReservations(request, response);
}
}
// ============================== // CANCEL // ============================== 
private void cancelReservation(HttpServletRequest request, HttpServletResponse response) 
		throws IOException { int reservationId = Integer.parseInt(request.getParameter("id")); 
		int roomId = Integer.parseInt(request.getParameter("roomId")); 
		reservationDAO.cancelReservation(reservationId, roomId);
		response.sendRedirect(request.getContextPath() + "/staff/reservations?action=list"); } 
}

