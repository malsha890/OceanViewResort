<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
<title>System Help</title>
<style>
.main-content{
margin-left:260px;
padding:40px;
font-family:Arial;
}

h1{
color:#0b5394;
margin-bottom:30px;
}

h2{
margin-top:25px;
color:#333;
}

ul{
margin-left:20px;
}
</style>
</head>

<body>

<div class="main-content">

<h1>Hotel Management System - Staff Help</h1>
<h2>Login to the System</h2>

<p>
Enter your username and password to login to the system.
</p>

<img src="${pageContext.request.contextPath}/images/login.png" 
     width="600" 
     alt="Login Page Screenshot">

<h2>1. Room Management</h2>
<p>
Staff members can manage hotel rooms using the Room Management module.
</p>
<img src="${pageContext.request.contextPath}/images/room management.png" 
     width="600" 
     alt=" Room Management Page Screenshot">
<ul>
<li>Add new rooms</li>
<li>Update room details</li>
<li>Delete rooms</li>
<li>Search rooms</li>
</ul>

<h2>2. Reservation Management</h2>
<p>
This section allows staff to manage customer reservations.
</p>
<img src="${pageContext.request.contextPath}/images/reservation management.png" 
     width="600" 
     alt=" Reservation  Management Page Screenshot">
<ul>
<li>Create new reservation</li>
<li>Update reservation details</li>
<li>Search reservations</li>
<li>View reservation information</li>
</ul>

<h2>3. Calculate & Print Bill</h2>
<p>
Staff can search a reservation using the Reservation ID and generate the bill.
</p>
<ul>
<li>Search reservation by ID</li>
<li>Calculate total payment</li>
<li>View bill</li>
<li>Print bill</li>
</ul>
<img src="${pageContext.request.contextPath}/images/view bill.png" 
     width="600" 
     alt=" view bill Page Screenshot">
<h2>4. Logout</h2>
<p>
Click the Exit button in the sidebar to logout from the system securely.
</p>

</div>

</body>
</html>