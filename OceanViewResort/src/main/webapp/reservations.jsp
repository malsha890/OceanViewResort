<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="java.util.List" %>
<%@ page import="com.oceanview.model.Reservation" %>
<!DOCTYPE html>
<html>
<head>
    <title>Reservation Management</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/style.css">

    <style>
        body { font-family: Arial; }
        .container { width: 95%; margin: 20px auto; }
        .form-section, .table-section {
            background: #f4f6f9;
            padding: 20px;
            margin-bottom: 30px;
            border-radius: 8px;
        }
        table {
            width: 100%;
            border-collapse: collapse;
        }
        table th, table td {
            padding: 10px;
            border: 1px solid #ccc;
            text-align: center;
        }
        table th { background: #007bff; color: white; }
        .btn {
            padding: 6px 12px;
            text-decoration: none;
            border-radius: 4px;
            color: white;
        }
        .btn-save { background: green; }
        .btn-cancel { background: red; }
        .btn-view-bill{ background:  #007bff; }
    </style>
</head>

<body>

<div class="container">

    <h2>Reservation Management</h2>

    <!-- ================= ADD RESERVATION FORM ================= -->
    <div class="form-section">
        <h3>Add Reservation</h3>

        <form action="${pageContext.request.contextPath}/staff/reservations"
              method="post">

            <input type="hidden" name="action" value="add"/>

            <label>Customer Name:</label>
            <input type="text" name="name" required/>

            <label>Phone:</label>
            <input type="text" name="phone"/>

            <label>Email:</label>
            <input type="email" name="email"/>

            <label>Address:</label>
            <input type="text" name="address" required/>

            <label>Room ID:</label>
            <input type="number" name="roomId" required/>

            <label>Room Type:</label>
            <select name="roomType" required>
                <option value="STANDARD">STANDARD</option>
                <option value="DELUXE">DELUXE</option>
                <option value="SUITE">SUITE</option>
            </select>

            <label>Check In:</label>
            <input type="date" name="checkIn" required/>

            <label>Check Out:</label>
            <input type="date" name="checkOut" required/>

            <br><br>
            <button type="submit" class="btn btn-save">
                Save Reservation
            </button>
        </form>
    </div>

    <!-- ================= RESERVATION LIST ================= -->
    <div class="table-section">
        <h3>Reservation List</h3>

        <table>
            <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Room ID</th>
                <th>Room Type</th>
                <th>Check In</th>
                <th>Check Out</th>
                <th>Status</th>
                <th>Action</th>
            </tr>


<c:forEach var="r" items="${reservations}">
                <tr>
                    <td>${r.reservationId}</td>                                   
                    <td>${r.customerName}</td>
                    <td>${r.roomId}</td>
                    <td>${r.roomType}</td>
                    <td>${r.checkIn}</td>
                    <td>${r.checkOut}</td>
                    <td>${r.status}</td>
                    <td>

                        <c:if test="${r.status != 'CANCELLED'}">
                            <a class="btn btn-cancel"
                               href="${pageContext.request.contextPath}/staff/reservations?action=cancel&id=${r.reservationId}&roomId=${r.roomId}">
                                Cancel
                            </a>
                            <a class="btn btn-view_bill"
   href="${pageContext.request.contextPath}/staff/bills?action=view&reservationId=${r.reservationId}">
   View Bill
</a>
                        </c:if>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </div>

</div>

</body>
</html>