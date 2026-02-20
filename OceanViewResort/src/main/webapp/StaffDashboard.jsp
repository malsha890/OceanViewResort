<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.*" %>
<%@ page import="com.oceanview.model.Staff" %>
<%
    Staff staff = (Staff) session.getAttribute("staff");
%>

<%
    Integer totalReservations =
        (Integer) request.getAttribute("totalReservations");

    Integer totalAvailableRooms =
        (Integer) request.getAttribute("totalAvailableRooms");

    Integer totalStaff =
        (Integer) request.getAttribute("totalStaff");

    if (totalReservations == null) totalReservations = 0;
    if (totalAvailableRooms == null) totalAvailableRooms = 0;
    if (totalStaff == null) totalStaff = 0;

    String staffName = (String) session.getAttribute("staffName");
    if (staffName == null) staffName = "Staff";
%>

<!DOCTYPE html>
<html>
<head>
<title>Staff Dashboard</title>

<style>
body {
    margin: 0;
    font-family: 'Segoe UI', sans-serif;
    background-color: #f4f6f9;
}

/* ===== SIDEBAR ===== */
.sidebar {
    width: 250px;
    height: 100vh;
    background: #1e293b;
    position: fixed;
    padding-top: 20px;
}

.sidebar h2 {
    color: #ffffff;
    text-align: center;
    margin-bottom: 30px;
}

.sidebar a {
    display: block;
    color: #cbd5e1;
    padding: 14px 25px;
    text-decoration: none;
    transition: 0.3s;
}

.sidebar a:hover {
    background: #334155;
    color: #ffffff;
}
.sidebar-btn {
    width: 100%;
    background: none;
    border: none;
    text-align: left;
    padding: 14px 25px;
    color: #cbd5e1;
    font-size: 15px;
    cursor: pointer;
    transition: 0.3s;
}

.sidebar-btn:hover {
    background: #dc2626;
    color: #ffffff;
}

/* ===== MAIN CONTENT ===== */
.main {
    margin-left: 250px;
    padding: 30px;
}

.header {
    background: #ffffff;
    padding: 20px;
    border-radius: 8px;
    margin-bottom: 25px;
    box-shadow: 0 3px 8px rgba(0,0,0,0.05);
}

.header h2 {
    margin: 0;
    color: #1e293b;
}

/* ===== DASHBOARD CARDS ===== */
.cards {
    display: flex;
    gap: 20px;
    margin-bottom: 30px;
}

.card {
    flex: 1;
    background: #ffffff;
    padding: 25px;
    border-radius: 10px;
    text-align: center;
    box-shadow: 0 3px 8px rgba(0,0,0,0.05);
    transition: 0.3s;
}

.card:hover {
    transform: translateY(-5px);
}

.card h3 {
    margin: 0;
    font-size: 16px;
    color: #64748b;
}

.card p {
    font-size: 30px;
    margin-top: 10px;
    font-weight: bold;
    color: #2563eb;
}

/* ===== ACTION BUTTONS ===== */
.actions {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
    gap: 20px;
}

.action-btn {
    background: #ffffff;
    padding: 20px;
    border-radius: 10px;
    text-align: center;
    text-decoration: none;
    font-weight: bold;
    color: #1e293b;
    box-shadow: 0 3px 8px rgba(0,0,0,0.05);
    transition: 0.3s;
}

.action-btn:hover {
    background: #2563eb;
    color: #ffffff;
}

.footer-buttons {
    margin-top: 40px;
    text-align: center;
}

.logout-btn, .exit-btn {
    padding: 10px 25px;
    border: none;
    border-radius: 5px;
    font-weight: bold;
    cursor: pointer;
    margin: 5px;
}

.logout-btn {
    background: #dc2626;
    color: white;
}

.exit-btn {
    background: #475569;
    color: white;
}
</style>

</head>
<body>

<!-- ===== SIDEBAR ===== -->
<div class="sidebar">
    <h2>Staff Dashboard</h2>

    
    <a href="${pageContext.request.contextPath}/staff/rooms">Manage Rooms</a>
    <a href="${pageContext.request.contextPath}/staff/reservations">Manage Reservations</a>
    <a href="${pageContext.request.contextPath}/staff/billing">Calculate & Print Bill</a>
    
    
    <!-- Help Section -->
    <a href="${pageContext.request.contextPath}/staff/help">Help</a>

    <!-- Exit Section -->
    <form action="${pageContext.request.contextPath}/exit" method="post">
        <button type="submit" class="sidebar-btn">Exit</button>
    </form>
</div>

<!-- ===== MAIN CONTENT ===== -->
<div class="main">

    <div class="header">
         <h2>Welcome <%= staff.getFullName() %></h2>
    </div>

    <!-- Dashboard Statistics -->
    <div class="cards">

        <div class="card">
            <h3>Total Reservations</h3>
            <p><%= totalReservations %></p>
        </div>

        <div class="card">
            <h3>Total Available Rooms</h3>
            <p><%= totalAvailableRooms %></p>
        </div>

        <div class="card">
            <h3>Total Staff</h3>
            <p><%= totalStaff %></p>
        </div>

    </div>

   

    

</div>

</body>
</html>

