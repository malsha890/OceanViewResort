package com.oceanview.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Bill {

    private int billId;
    private int reservationId;
    private LocalDate issueDate;
    private BigDecimal roomCharge;
    private BigDecimal tax;
    private BigDecimal serviceCharge;
    private BigDecimal totalAmount;
    private String status;

    public Bill() {}

    public int getBillId() { return billId; }
    public void setBillId(int billId) { this.billId = billId; }

    public int getReservationId() { return reservationId; }
    public void setReservationId(int reservationId) { this.reservationId = reservationId; }

    public LocalDate getIssueDate() { return issueDate; }
    public void setIssueDate(LocalDate issueDate) { this.issueDate = issueDate; }

    public BigDecimal getRoomCharge() { return roomCharge; }
    public void setRoomCharge(BigDecimal roomCharge) { this.roomCharge = roomCharge; }

    public BigDecimal getTax() { return tax; }
    public void setTax(BigDecimal tax) { this.tax = tax; }

    public BigDecimal getServiceCharge() { return serviceCharge; }
    public void setServiceCharge(BigDecimal serviceCharge) { this.serviceCharge = serviceCharge; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
