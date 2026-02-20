<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.*" %>
<%@ page import="com.oceanview.model.Reservation" %>

<%
List<Reservation> list =
    (List<Reservation>) request.getAttribute("list");

if (list == null) list = new ArrayList<>();
%>


<!DOCTYPE html>
<html>
<head>
<title>Manage Reservations</title>

<style>
body {
    font-family: 'Segoe UI', sans-serif;
    background: #f4f6f9;
    margin: 0;
    padding: 20px;
}

h2 {
    color: #1e293b;
}

.container {
    background: white;
    padding: 20px;
    border-radius: 8px;
    box-shadow: 0 3px 8px rgba(0,0,0,0.05);
}

form input, form select {
    padding: 8px;
    margin: 5px;
}

button {
    padding: 6px 12px;
    border: none;
    cursor: pointer;
    border-radius: 4px;
}

.add-btn { background:#2563eb; color:white; }
.edit-btn { background:#f59e0b; color:white; }
.delete-btn { background:#dc2626; color:white; }

table {
    width:100%;
    border-collapse: collapse;
    margin-top:20px;
}

th, td {
    padding:10px;
    border-bottom:1px solid #ddd;
    text-align:center;
}

th {
    background:#1e293b;
    color:white;
}
.search-box {
    margin-bottom: 15px;
}
</style>
</head>
<body>

<h2>Reservation Management</h2>

<div class="container">

<!-- ================= SEARCH ================= -->
<div class="search-box">
<form method="get" action="${pageContext.request.contextPath}/staff/reservations">
    <input type="hidden" name="action" value="search">
    <input type="text" name="keyword" placeholder="Search by Name or Phone" required>
    <button class="add-btn">Search</button>
    <a href="${pageContext.request.contextPath}/staff/reservations">
        <button type="button">Reset</button>
    </a>
</form>
</div>

<!-- ================= ADD FORM ================= -->
<h3>Add New Reservation</h3>

<form method="post" action="${pageContext.request.contextPath}/staff/reservations">
    <input type="hidden" name="action" value="add">

    <input type="text" name="name" placeholder="Customer Name" required>
    <input type="text" name="phone" placeholder="Phone" required>
    <input type="email" name="email" placeholder="Email" required>
    <input type="number" name="roomId" placeholder="Room ID" required>

    <input type="date" name="checkIn" required>
    <input type="date" name="checkOut" required>

    

    <select name="status">
        <option value="PENDING">PENDING</option>
        <option value="CONFIRMED">CONFIRMED</option>
        <option value="CANCELLED">CANCELLED</option>
    </select>

    <button type="submit" class="add-btn">Add</button>
</form>

<!-- ================= TABLE ================= -->
<h3>All Reservations</h3>

<table>
<tr>
    <th>ID</th>
    <th>Name</th>
    <th>Phone</th>
    <th>Email</th>
    <th>RoomID</th>
    <th>Room-Type</th>
    <th>Check-In</th>
    <th>Check-Out</th>
    
    <th>Status</th>
    <th>Actions</th>
</tr>

<% for (Reservation r : list) { %>
<tr>
    <td><%= r.getReservationId() %></td>
    <td><%= r.getCustomerName() %></td>
    <td><%= r.getCustomerPhone() %></td>
    <td><%= r.getCustomerEmail() %></td>
    <td><%= r.getRoomId() %></td>
    
    <td><%= r.getCheckIn() %></td>
    <td><%= r.getCheckOut() %></td>
    
    <td><%= r.getStatus() %></td>
    <td>

        <!-- EDIT -->
        <form method="post"
              action="${pageContext.request.contextPath}/staff/reservations"
              style="display:inline;">
            <input type="hidden" name="action" value="update">
            <input type="hidden" name="id" value="<%= r.getReservationId() %>">

            <input type="hidden" name="name" value="<%= r.getCustomerName() %>">
            <input type="hidden" name="phone" value="<%= r.getCustomerPhone() %>">
            <input type="hidden" name="email" value="<%= r.getCustomerEmail() %>">
            <input type="hidden" name="roomId" value="<%= r.getRoomId() %>">
            <input type="hidden" name="checkIn" value="<%= r.getCheckIn() %>">
            <input type="hidden" name="checkOut" value="<%= r.getCheckOut() %>">
            
            <input type="hidden" name="status" value="CONFIRMED">

            <button class="edit-btn">Quick Confirm</button>
        </form>

        <!-- DELETE -->
        <form method="post"
              action="${pageContext.request.contextPath}/staff/reservations"
              style="display:inline;"
              onsubmit="return confirm('Delete this reservation?');">

            <input type="hidden" name="action" value="delete">
            <input type="hidden" name="id" value="<%= r.getReservationId() %>">
            <input type="hidden" name="roomId" value="<%= r.getRoomId() %>">

            <button class="delete-btn">Delete</button>
        </form>

    </td>
</tr>
<% } %>

</table>

</div>

</body>
</html>
