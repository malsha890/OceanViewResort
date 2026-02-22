package com.oceanview.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Reservation {

    private int reservationId;
    private String customerName;
    private String customerPhone;
    private String customerEmail;
    private int roomId;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private String status; // PENDING, CONFIRMED, CANCELLED
    private LocalDateTime createdAt;
    private String roomType;
    private String customerAddress;

    // =========================
    // Constructors
    // =========================

    public Reservation() {
    }

    public Reservation(String customerName, String customerPhone,
                       String customerEmail, int roomId,
                       LocalDate checkIn, LocalDate checkOut,
                       String status, String roomType,
                       String customerAddress) {

        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.customerEmail = customerEmail;
        this.roomId = roomId;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.status = status;
        this.roomType = roomType;
        this.customerAddress = customerAddress;
    }

    // =========================
    // Getters and Setters
    // =========================

    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public LocalDate getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(LocalDate checkIn) {
        this.checkIn = checkIn;
    }

    public LocalDate getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(LocalDate checkOut) {
        this.checkOut = checkOut;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }
}