package com.oceanview.dao;

import java.math.BigDecimal;
import java.sql.*;
import java.sql.Date;
import java.util.*;
import com.oceanview.model.Room;
import com.oceanview.util.DBConnection;

public class RoomDAO {

	

	 public RoomDAO() {
	    }



	// Add Room
    public void addRoom(Room r) throws Exception {
        String sql = "INSERT INTO rooms(room_number, room_type, price_per_night, capacity, status) VALUES(?,?,?,?,?)";
        try (Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(sql)){
        ps.setString(1, r.getRoomNumber());
        ps.setString(2, r.getRoomType());
        ps.setDouble(3, r.getPricePerNight());
        ps.setInt(4, r.getCapacity());
        ps.setString(5, r.getStatus());
        ps.executeUpdate();
        }
    }

    // Get All Rooms
    public List<Room> getAllRooms() throws Exception {
        List<Room> list = new ArrayList<>();
        String sql = "SELECT * FROM rooms";
        try (Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(sql)){
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Room r = new Room();
            r.setRoomId(rs.getInt("room_id"));
            r.setRoomNumber(rs.getString("room_number"));
            r.setRoomType(rs.getString("room_type"));
            r.setPricePerNight(rs.getDouble("price_per_night"));
            r.setCapacity(rs.getInt("capacity"));
            r.setStatus(rs.getString("status"));
            list.add(r);
        }
        return list;
    }
    }
    // Update Room
    public void updateRoom(Room r) throws Exception {
        String sql = "UPDATE rooms SET room_number=?, room_type=?, price_per_night=?, capacity=?, status=? WHERE room_id=?";
        try (Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(sql)){
        ps.setString(1, r.getRoomNumber());
        ps.setString(2, r.getRoomType());
        ps.setDouble(3, r.getPricePerNight());
        ps.setInt(4, r.getCapacity());
        ps.setString(5, r.getStatus());
        ps.setInt(6, r.getRoomId());
        ps.executeUpdate();
    }
    }
    // ❌ Prevent deleting BOOKED room
    public boolean deleteRoom(int id) throws Exception {

        String check = "SELECT status FROM rooms WHERE room_id=?";
        try (Connection con = DBConnection.getConnection();
        PreparedStatement psCheck = con.prepareStatement(check)){
        psCheck.setInt(1, id);
        ResultSet rs = psCheck.executeQuery();

        if (rs.next()) {
            if (rs.getString("status").equals("BOOKED")) {
                return false; // Cannot delete
            }
        }

        String sql = "DELETE FROM rooms WHERE room_id=?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, id);
        ps.executeUpdate();
        return true;
    }
    }
    // 🔄 Auto update room status
    public void updateRoomStatus(int roomId, String status) throws Exception {
        String sql = "UPDATE rooms SET status=? WHERE room_id=?";
        try (Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(sql)){
        ps.setString(1, status);
        ps.setInt(2, roomId);
        ps.executeUpdate();
    }
    }
    // Search
    public List<Room> searchRoom(String keyword) throws Exception {
        List<Room> list = new ArrayList<>();
        String sql = "SELECT * FROM rooms WHERE room_number LIKE ?";
        try (Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(sql)){
        ps.setString(1, "%" + keyword + "%");
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Room r = new Room();
            r.setRoomId(rs.getInt("room_id"));
            r.setRoomNumber(rs.getString("room_number"));
            r.setRoomType(rs.getString("room_type"));
            r.setPricePerNight(rs.getDouble("price_per_night"));
            r.setCapacity(rs.getInt("capacity"));
            r.setStatus(rs.getString("status"));
            list.add(r);
        }
        return list;
    }
    }
    public boolean isRoomAvailable(int roomId, Date checkIn, Date checkOut) throws Exception {

        String sql = "SELECT COUNT(*) FROM reservations WHERE room_id=? " +
                     "AND status='CONFIRMED' " +
                     "AND (check_in < ? AND check_out > ?)";
        try (Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(sql)){
        ps.setInt(1, roomId);
        ps.setDate(2, checkOut);
        ps.setDate(3, checkIn);

        ResultSet rs = ps.executeQuery();
        rs.next();

        return rs.getInt(1) == 0;
    }
        
}
    public BigDecimal getRoomPriceById(int roomId) {

        BigDecimal price = BigDecimal.ZERO;

        try (Connection con = DBConnection.getConnection()) {

            String sql = "SELECT price_per_night FROM rooms WHERE room_id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, roomId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                price = rs.getBigDecimal("price_per_night");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return price;
    }
}