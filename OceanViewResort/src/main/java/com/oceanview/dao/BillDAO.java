package com.oceanview.dao;

import com.oceanview.model.Bill;
import com.oceanview.model.Reservation;
import com.oceanview.util.DBConnection;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import java.time.temporal.ChronoUnit;

public class BillDAO {

    private ReservationDAO reservationDAO = new ReservationDAO();
    private RoomDAO roomDAO = new RoomDAO();

    // ==========================================
    // CREATE BILL
    // ==========================================
    public void createBill(int reservationId) {

        try (Connection con = DBConnection.getConnection()) {

            // 1️⃣ Get reservation details
            Reservation reservation = reservationDAO.getReservationById(reservationId);

            if (reservation == null) {
                System.out.println("Reservation not found!");
                return;
            }

            // 2️⃣ Calculate number of days
            long days = ChronoUnit.DAYS.between(
                    reservation.getCheckIn(),
                    reservation.getCheckOut());

            if (days <= 0) {
                days = 1;
            }

            // 3️⃣ Get room rate from DB
            BigDecimal roomRate = roomDAO.getRoomPriceById(reservation.getRoomId());

            // 4️⃣ Calculate charges
            BigDecimal roomCharge = roomRate.multiply(BigDecimal.valueOf(days));

            BigDecimal tax = roomCharge
                    .multiply(new BigDecimal("0.05"))
                    .setScale(2, RoundingMode.HALF_UP);

            BigDecimal serviceCharge = roomCharge
                    .multiply(new BigDecimal("0.10"))
                    .setScale(2, RoundingMode.HALF_UP);

            BigDecimal totalAmount = roomCharge
                    .add(tax)
                    .add(serviceCharge)
                    .setScale(2, RoundingMode.HALF_UP);

            // 5️⃣ Insert into database
            String sql = "INSERT INTO bills " +
                    "(reservation_id, issue_date, room_charge, tax, service_charge, total_amount, status) " +
                    "VALUES (?, CURDATE(), ?, ?, ?, ?, 'UNPAID')";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, reservationId);
            ps.setBigDecimal(2, roomCharge);
            ps.setBigDecimal(3, tax);
            ps.setBigDecimal(4, serviceCharge);
            ps.setBigDecimal(5, totalAmount);

            ps.executeUpdate();

            System.out.println("Bill created successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ==========================================
    // GET BILL BY RESERVATION
    // ==========================================
    public Bill getBillByReservation(int reservationId) {

        Bill bill = null;

        try (Connection con = DBConnection.getConnection()) {

            String sql = "SELECT * FROM bills WHERE reservation_id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, reservationId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                bill = new Bill();

                bill.setBillId(rs.getInt("bill_id"));
                bill.setReservationId(rs.getInt("reservation_id"));
                bill.setRoomCharge(rs.getBigDecimal("room_charge"));
                bill.setTax(rs.getBigDecimal("tax"));
                bill.setServiceCharge(rs.getBigDecimal("service_charge"));
                bill.setTotalAmount(rs.getBigDecimal("total_amount"));
                bill.setStatus(rs.getString("status"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return bill;
    }

    // ==========================================
    // MARK BILL AS PAID
    // ==========================================
    public void markAsPaid(int billId) {

        try (Connection con = DBConnection.getConnection()) {

            String sql = "UPDATE bills SET status='PAID' WHERE bill_id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, billId);

            ps.executeUpdate();

            System.out.println("Bill marked as PAID!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}