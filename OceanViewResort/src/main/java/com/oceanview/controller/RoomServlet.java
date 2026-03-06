package com.oceanview.controller;

import com.google.gson.Gson;
import com.oceanview.dao.RoomDAO;
import com.oceanview.model.Room;
import com.oceanview.util.DBConnection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@WebServlet({"/staff/rooms", "/api/rooms"})
public class RoomServlet extends HttpServlet {

    Gson gson = new Gson(); 

    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getServletPath();

       
        if ("/api/rooms".equals(path)) {
            handleAPIGet(request, response);
            return;
        }

        
        String action = request.getParameter("action");
        try (Connection con = DBConnection.getConnection()) {
            RoomDAO dao = new RoomDAO();
            if ("search".equals(action)) {
                String keyword = request.getParameter("keyword");
                request.setAttribute("list", dao.searchRoom(keyword));
            } else {
                request.setAttribute("list", dao.getAllRooms());
            }
            request.getRequestDispatcher("/rooms.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getServletPath();

     
        if ("/api/rooms".equals(path)) {
            handleAPIPost(request, response);
            return;
        }
        
        String action = request.getParameter("action");
        try (Connection conn = DBConnection.getConnection()) {
            RoomDAO dao = new RoomDAO();
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

  
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        try {
            
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = request.getReader().readLine()) != null) {
                sb.append(line);
            }

            
            Room r = gson.fromJson(sb.toString(), Room.class);

            RoomDAO dao = new RoomDAO();
            dao.updateRoom(r); 

            Map<String, String> result = new HashMap<>();
            result.put("status", "success");
            result.put("message", "Room updated successfully");
            out.write(gson.toJson(result));

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            Map<String, String> error = new HashMap<>();
            error.put("status", "error");
            error.put("message", e.getMessage());
            out.write(gson.toJson(error));
        }
    }

   
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        try {
            int id = Integer.parseInt(request.getParameter("id"));

            RoomDAO dao = new RoomDAO();
            dao.deleteRoom(id); 

            Map<String, String> result = new HashMap<>();
            result.put("status", "success");
            result.put("message", "Room deleted successfully");
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
            RoomDAO dao = new RoomDAO();
            String idParam    = request.getParameter("id");
            String keyword    = request.getParameter("keyword");

            if (idParam != null) {
               
                Room room = dao.getRoomById(Integer.parseInt(idParam));
                if (room != null) {
                    out.write(gson.toJson(room));
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    out.write("{\"status\": \"error\", \"message\": \"Room not found\"}");
                }

            } else if (keyword != null) {
               
                List<Room> results = dao.searchRoom(keyword);
                out.write(gson.toJson(results));

            } else {
                
                List<Room> rooms = dao.getAllRooms();
                out.write(gson.toJson(rooms));
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
            // Read JSON body
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = request.getReader().readLine()) != null) {
                sb.append(line);
            }

        
            Room r = gson.fromJson(sb.toString(), Room.class);

            // Basic validation
            if (r.getRoomNumber() == null || r.getRoomType() == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.write("{\"status\": \"error\", \"message\": \"Room number and type are required\"}");
                return;
            }

            RoomDAO dao = new RoomDAO();
            dao.addRoom(r); 

            response.setStatus(HttpServletResponse.SC_CREATED);
            out.write("{\"status\": \"success\", \"message\": \"Room added successfully\"}");

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.write("{\"status\": \"error\", \"message\": \"" + e.getMessage() + "\"}");
        }
    }
}
