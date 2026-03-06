<%@ page contentType="text/html;charset=UTF-8" %>

<html>
<head>
<title>Admin Dashboard</title>

<style>

body{
font-family: Arial;
margin:0;
background:#f4f6f9;
}

.sidebar{
width:220px;
height:100vh;
background:#2c3e50;
position:fixed;
padding-top:20px;
}

.sidebar h2{
color:white;
text-align:center;
}

.sidebar a{
display:block;
color:white;
padding:15px;
text-decoration:none;
}

.sidebar a:hover{
background:#34495e;
}

.main{
margin-left:220px;
padding:30px;
}

.card{
background:white;
padding:20px;
margin:20px;
border-radius:8px;
box-shadow:0 0 10px #ccc;
display:inline-block;
width:200px;
text-align:center;
}
</style>
</head>

<body>

<div class="sidebar">

<h2>Admin Panel</h2>

<a href="${pageContext.request.contextPath}/admin/staff">Staff Management</a>

<a href="${pageContext.request.contextPath}/admin/reports">Reports</a>

<a href="${pageContext.request.contextPath}/logout">Logout</a>

</div>

<div class="main">

<h1>Welcome Admin</h1>

<div class="card">
<h3>Staff</h3>
<p>Manage hotel staff</p>
</div>

<div class="card">
<h3>Reports</h3>
<p>View system reports</p>
</div>

</div>

</body>
</html>