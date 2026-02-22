package com.oceanview.dao;

import com.oceanview.model.Reservation;
import com.oceanview.util.DBConnection;

import java.sql.*;
import java.util.*;
import java.sql.Date;
public class ReservationDAO {

    RoomDAO roomDAO = new RoomDAO(); // ✅ FIXED

    // ✅ ADD RESERVATION
    public void addReservation(Reservation r) {

        String sql = "INSERT INTO reservations " +
                "(customer_name, customer_phone, customer_email, room_id, check_in, check_out, status, room_type, customer_address) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
        		
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, r.getCustomerName());
            ps.setString(2, r.getCustomerPhone());
            ps.setString(3, r.getCustomerEmail());
            ps.setInt(4, r.getRoomId());
            ps.setDate(5, Date.valueOf(r.getCheckIn()));
            ps.setDate(6, Date.valueOf(r.getCheckOut()));
            ps.setString(7, r.getStatus() != null ? r.getStatus() : "CONFIRMED");
            ps.setString(8, r.getRoomType());
            ps.setString(9, r.getCustomerAddress());

            ps.executeUpdate();

            // 🔥 Update Room
            roomDAO.updateRoomStatus(r.getRoomId(), "BOOKED");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("ADD RESERVATION METHOD CALLED");
        System.out.println("DB Connected");
    }

    // ✅ CANCEL
    public void cancelReservation(int reservationId, int roomId) {

        String sql = "UPDATE reservations SET status='CANCELLED' WHERE reservation_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, reservationId);
            ps.executeUpdate();

            roomDAO.updateRoomStatus(roomId, "AVAILABLE");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ✅ GET ALL
    public List<Reservation> getAllReservations() {

        List<Reservation> list = new ArrayList<>();

        String sql = "SELECT * FROM reservations";

        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {

                Reservation r = new Reservation();

                r.setReservationId(rs.getInt("reservation_id"));
                r.setCustomerName(rs.getString("customer_name"));
                r.setCustomerPhone(rs.getString("customer_phone"));
                r.setCustomerEmail(rs.getString("customer_email"));
                r.setRoomId(rs.getInt("room_id"));
                r.setStatus(rs.getString("status"));
                r.setRoomType(rs.getString("room_type"));
                r.setCustomerAddress(rs.getString("customer_address"));

                r.setCheckIn(rs.getDate("check_in").toLocalDate());
                r.setCheckOut(rs.getDate("check_out").toLocalDate());

                if (rs.getTimestamp("created_at") != null) {
                    r.setCreatedAt(
                            rs.getTimestamp("created_at").toLocalDateTime()
                    );
                }

                list.add(r);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}