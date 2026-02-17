<%@ page import="com.oceanview.model.Staff" %>
<%
    Staff staff = (Staff) session.getAttribute("staff");
%>

<!DOCTYPE html>
<html>
<head>
    <title>Staff Dashboard</title>
</head>
<body>
    <h2>Welcome <%= staff.getFullName() %></h2>
    <h3>Role: <%= staff.getRole() %></h3>
    <a href="login.jsp">Logout</a>
</body>
</html>
