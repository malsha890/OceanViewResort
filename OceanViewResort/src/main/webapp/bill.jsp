<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.oceanview.model.Bill" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%
    Bill bill = (Bill) request.getAttribute("bill");
%>

<!DOCTYPE html>
<html>
<head>
    <title>Bill - OceanView Resort</title>

    <style>
        body {
            font-family: Arial;
            margin: 30px;
        }

        .bill-container {
            width: 600px;
            margin: auto;
            border: 1px solid #ccc;
            padding: 20px;
            border-radius: 8px;
        }

        h2 {
            text-align: center;
        }

        table {
            width: 100%;
            border-collapse: collapse;
        }

        table td {
            padding: 10px;
            border: 1px solid #ddd;
        }

        .total-row {
            font-weight: bold;
            background: #f4f6f9;
        }

        .btn {
            padding: 8px 15px;
            text-decoration: none;
            border-radius: 4px;
            color: white;
            display: inline-block;
            margin-top: 15px;
        }

        .btn-pay {
            background: green;
        }

        .btn-back {
            background: #007bff;
        }

        .btn-print {
            background: orange;
        }

        @media print {
            .no-print {
                display: none;
            }
        }
    </style>
</head>

<body>

<div class="bill-container">

    <h2>OceanView Resort</h2>
    <h3 style="text-align:center;">Official Invoice</h3>

    <c:if test="${bill != null}">

        <table>
            <tr>
                <td><b>Room Charge</b></td>
                <td>${bill.roomCharge}</td>
            </tr>

            <tr>
                <td><b>Service Charge (10%)</b></td>
                <td>${bill.serviceCharge}</td>
            </tr>

            <tr>
                <td><b>Tax (5%)</b></td>
                <td>${bill.tax}</td>
            </tr>

            <tr class="total-row">
                <td><b>Total Amount</b></td>
                <td><b>${bill.totalAmount}</b></td>
            </tr>

            <tr>
                <td><b>Status</b></td>
                <td>${bill.status}</td>
            </tr>
        </table>

        <div class="no-print">

            <c:if test="${bill.status == 'UNPAID'}">
                <a class="btn btn-pay"
                   href="${pageContext.request.contextPath}/staff/bills?action=pay&billId=${bill.billId}">
                    Mark as Paid
                </a>
            </c:if>

            <a class="btn btn-print"
               href="#"
               onclick="window.print()">
                Print Bill
            </a>

            <a class="btn btn-back"
               href="${pageContext.request.contextPath}/staff/reservations?action=list">
                Back to Reservations
            </a>

        </div>

    </c:if>

    <c:if test="${bill == null}">
        <p>No bill data available.</p>
    </c:if>

</div>

</body>
</html>