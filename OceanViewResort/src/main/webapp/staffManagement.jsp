<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.*, com.oceanview.model.Staff" %>
<% if (request.getAttribute("error") != null) { %>
    <p style="color:red;">
        <%= request.getAttribute("error") %>
    </p>
<% } %>

<html>
<head>
<title>Admin Dashboard</title>
<script>
function togglePassword() {
    var passwordField = document.getElementById("password");

    if (passwordField.type === "password") {
        passwordField.type = "text";
    } else {
        passwordField.type = "password";
    }
}

function checkPasswordStrength() {
    var password = document.getElementById("password").value;
    var message = document.getElementById("strengthMessage");

    var strength = 0;

    if (password.length >= 8) strength++;
    if (/[A-Z]/.test(password)) strength++;
    if (/[a-z]/.test(password)) strength++;
    if (/[0-9]/.test(password)) strength++;
    if (/[^A-Za-z0-9]/.test(password)) strength++;

    switch(strength) {
        case 0:
        case 1:
            message.innerHTML = "Weak password";
            message.style.color = "red";
            break;
        case 2:
        case 3:
            message.innerHTML = "Medium strength";
            message.style.color = "orange";
            break;
        case 4:
        case 5:
            message.innerHTML = "Strong password";
            message.style.color = "green";
            break;
    }
}
</script>
</head>
<body>
<form method="get" action="${pageContext.request.contextPath}/admin/staff">
    <input type="text" name="search" placeholder="Search staff...">
    <button type="submit">Search</button>
</form>

<hr>

<h2>Admin Dashboard - Manage Staff</h2>



<%
    Staff editStaff = (Staff) request.getAttribute("editStaff");
%>

<h3><%= (editStaff != null) ? "Edit Staff" : "Add New Staff" %></h3>

<form action="${pageContext.request.contextPath}/admin/staff" method="post">


<% if (editStaff != null) { %>
    <input type="hidden" name="action" value="update">
    <input type="hidden" name="id" value="<%= editStaff.getStaffId() %>">
<% } else { %>
    <input type="hidden" name="action" value="add">
<% } %>

Name:
<input type="text" name="fullName"
       value="<%= (editStaff!=null)?editStaff.getFullName():"" %>" required><br><br>

Email:
<input type="email" name="email"
       value="<%= (editStaff!=null)?editStaff.getEmail():"" %>" required><br><br>

<% if (editStaff == null) { %>

Password: 
<input type="password" 
       id="password"
       name="password" 
       required
       onkeyup="checkPasswordStrength()">

<button type="button" onclick="togglePassword()">Show</button>

<br>
<small id="strengthMessage"></small>

<br><br>

<% } %>

Role:
<select name="role">
    <option value="STAFF"
        <%= (editStaff!=null && "STAFF".equals(editStaff.getRole()))?"selected":"" %>>
        Staff
    </option>
    <option value="ADMIN"
        <%= (editStaff!=null && "ADMIN".equals(editStaff.getRole()))?"selected":"" %>>
        Admin
    </option>
</select><br><br>

<button type="submit">
    <%= (editStaff != null) ? "Update" : "Add" %>
</button>

</form>


<hr>

<h3>Staff List</h3>

<table border="1" cellpadding="5">
<tr>
    <th>ID</th>
    <th>Name</th>
    <th>Email</th>
    <th>Role</th>
    <th>Action</th>
</tr>

<%
    List<Staff> staffList = (List<Staff>) request.getAttribute("staffList");
    if (staffList != null) {
        for (Staff s : staffList) {
%>

<tr>
    <td><%= s.getStaffId() %></td>
    <td><%= s.getFullName() %></td>
    <td><%= s.getEmail() %></td>
    <td><%= s.getRole() %></td>
<td>

    <!-- EDIT BUTTON -->
    <a href="${pageContext.request.contextPath}/admin/staff?edit=<%= s.getStaffId() %>">
        <button type="submit" class="sidebar-btn">Edit</button>
    </a>

    &nbsp; | &nbsp;

    <!-- DELETE FORM -->
    <form action="${pageContext.request.contextPath}/admin/staff"
          method="post"
          style="display:inline;"
          onsubmit="return confirm('Are you sure you want to delete this staff?');">

        <input type="hidden" name="action" value="delete">
        <input type="hidden" name="id" value="<%= s.getStaffId() %>">

        <button type="submit">Delete</button>

    </form>

</td>

</tr>

<%
        }
    }
%>

</table>

<br>
<form action="${pageContext.request.contextPath}/logout" method="post">
        <button type="submit" class="sidebar-btn">Exit</button>
    </form>

</body>
</html>

