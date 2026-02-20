package com.oceanview.dao;

import java.sql.*;
import java.util.*;

import com.oceanview.model.Reservation;

public class ReservationDAO {

    private Connection conn;

    public ReservationDAO(Connection conn) {
        this.conn = conn;
    }

    // ================= ADD =================
    public void addReservation(Reservation r) throws Exception {

        String sql = "INSERT INTO reservations " +
                "(customer_name, customer_phone, customer_email, room_id,  check_in, check_out,  status) " +
                "VALUES (?,?,?,?,?,?,?,?,?)";

        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setString(1, r.getCustomerName());
        ps.setString(2, r.getCustomerPhone());
        ps.setString(3, r.getCustomerEmail());
        ps.setInt(4, r.getRoomId());
       
        ps.setDate(5, new java.sql.Date(r.getCheckIn().getTime()));
        ps.setDate(6, new java.sql.Date(r.getCheckOut().getTime()));
        ps.setString(7, r.getStatus());
        

        int row = ps.executeUpdate();

        RoomDAO roomDAO = new RoomDAO(conn);
        roomDAO.updateRoomStatus(r.getRoomId(), "BOOKED");
    }

    // ================= UPDATE =================
    public boolean updateReservation(Reservation r) throws Exception {

        String sql = "UPDATE reservations SET " +
                "customer_name=?, customer_phone=?, customer_email=?, " +
                "check_in=?, check_out=?, total_amount=?, status=? " +
                "WHERE reservation_id=?";

        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setString(1, r.getCustomerName());
        ps.setString(2, r.getCustomerPhone());
        ps.setString(3, r.getCustomerEmail());
        ps.setDate(4, new java.sql.Date(r.getCheckIn().getTime()));
        ps.setDate(5, new java.sql.Date(r.getCheckOut().getTime()));
        ps.setDouble(6, r.getTotalAmount());
        ps.setString(7, r.getStatus());
        ps.setInt(8, r.getReservationId());

        return ps.executeUpdate() > 0;
    }

    // ================= DELETE =================
    public void deleteReservation(int reservationId, int roomId2) throws Exception {

        // First get room_id from reservation
        String getRoomSql = "SELECT room_id FROM reservations WHERE reservation_id=?";
        PreparedStatement ps1 = conn.prepareStatement(getRoomSql);
        ps1.setInt(1, reservationId);
        ResultSet rs = ps1.executeQuery();

        int roomId = 0;
        if (rs.next()) {
            roomId = rs.getInt("room_id");
        }

        // Update reservation status
        String sql = "UPDATE reservations SET status='CANCELLED' WHERE reservation_id=?";
        PreparedStatement ps2 = conn.prepareStatement(sql);
        ps2.setInt(1, reservationId);
        ps2.executeUpdate();

        // 🔄 Set room back to AVAILABLE
        RoomDAO roomDAO = new RoomDAO(conn);
        roomDAO.updateRoomStatus(roomId, "AVAILABLE");
    }

    // ================= SEARCH =================
    public List<Reservation> search(String keyword) throws Exception {

        List<Reservation> list = new ArrayList<>();

        String sql = "SELECT * FROM reservations " +
                "WHERE customer_name LIKE ? OR customer_phone LIKE ?";

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, "%" + keyword + "%");
        ps.setString(2, "%" + keyword + "%");

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            list.add(extractReservation(rs));
        }

        return list;
    }

    // ================= GET ALL =================
    public List<Reservation> getAll() throws Exception {

        List<Reservation> list = new ArrayList<>();
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM reservations ORDER BY reservation_id DESC");

        while (rs.next()) {
            list.add(extractReservation(rs));
        }

        return list;
    }

    // ================= ROOM AVAILABILITY =================
    private void updateRoomAvailability(int roomId, boolean available) throws Exception {

        String sql = "UPDATE rooms SET available=? WHERE room_id=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setBoolean(1, available);
        ps.setInt(2, roomId);
        ps.executeUpdate();
    }

    // ================= HELPER =================
    private Reservation extractReservation(ResultSet rs) throws Exception {

        Reservation r = new Reservation();

        r.setReservationId(rs.getInt("reservation_id"));
        r.setCustomerName(rs.getString("customer_name"));
        r.setCustomerPhone(rs.getString("customer_phone"));
        r.setCustomerEmail(rs.getString("customer_email"));
        r.setRoomId(rs.getInt("room_id"));
        
        r.setCheckIn(rs.getDate("check_in"));
        r.setCheckOut(rs.getDate("check_out"));
        
        r.setStatus(rs.getString("status"));
        r.setCreatedAt(rs.getTimestamp("created_at"));

        return r;
    }

	
}
