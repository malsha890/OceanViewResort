

package com.oceanview.controller;


import java.io.IOException;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oceanview.dao.RoomDAO;
import com.oceanview.model.Room;
import com.oceanview.util.DBConnection;


@WebServlet("/staff/rooms")
public class RoomServlet extends HttpServlet {
	

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        try (Connection conn = DBConnection.getConnection()) {

            RoomDAO dao = new RoomDAO(conn);

            if ("search".equals(action)) {
                String keyword = request.getParameter("keyword");
                request.setAttribute("list", dao.searchRoom(keyword));
            } else {
                request.setAttribute("list", dao.getAllRooms());
            }

            request.getRequestDispatcher("/rooms.jsp")
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

            RoomDAO dao = new RoomDAO(conn);
            Room r = new Room();

            if ("add".equals(action)) {

                r.setRoomNumber(request.getParameter("roomNumber"));
                r.setRoomType(request.getParameter("roomType"));
                r.setPricePerNight(Double.parseDouble(request.getParameter("price")));
                r.setCapacity(Integer.parseInt(request.getParameter("capacity")));
                r.setStatus(request.getParameter("status"));

                dao.addRoom(r);

            } else if ("update".equals(action)) {

                r.setRoomId(Integer.parseInt(request.getParameter("id")));
                r.setRoomNumber(request.getParameter("roomNumber"));
                r.setRoomType(request.getParameter("roomType"));
                r.setPricePerNight(Double.parseDouble(request.getParameter("price")));
                r.setCapacity(Integer.parseInt(request.getParameter("capacity")));
                r.setStatus(request.getParameter("status"));

                dao.updateRoom(r);

            } else if ("delete".equals(action)) {

                int id = Integer.parseInt(request.getParameter("id"));
                dao.deleteRoom(id);
            }

            response.sendRedirect(request.getContextPath() + "/staff/rooms");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
