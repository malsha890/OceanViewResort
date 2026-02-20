<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.*, com.oceanview.model.Room" %>

<%
List<Room> list = (List<Room>) request.getAttribute("list");
if (list == null) {
    list = new ArrayList<>();
}
%>

<!DOCTYPE html>
<html>
<head>
<title>Room Management</title>
<style>
body { font-family: Arial; background:#f4f6f9; }
.card { background:white; padding:20px; margin:20px; border-radius:10px; }
table { width:100%; border-collapse: collapse; }
th, td { padding:10px; border-bottom:1px solid #ddd; text-align:center; }
button { padding:6px 12px; cursor:pointer; }
.add-btn { background:#28a745; color:white; border:none; }
.update-btn { background:#007bff; color:white; border:none; }
.delete-btn { background:#dc3545; color:white; border:none; }
</style>
</head>
<body>

<div class="card">
<h2>Room Management</h2>

<form action="${pageContext.request.contextPath}/staff/rooms" method="post">
    Room Number: <input type="text" name="roomNumber" required>
    
    Type:
    <select name="roomType">
        <option value="STANDARD">STANDARD</option>
        <option value="DELUXE">DELUXE</option>
        <option value="SUITE">SUITE</option>
    </select>
    
    Price: <input type="number" step="0.01" name="price" required>
    
    Capacity: <input type="number" name="capacity" required>
    
    <input type="hidden" name="status" value="AVAILABLE">
    
    <button type="submit" name="action" value="add" class="add-btn">
        Add Room
    </button>
</form>
</div>

<div class="card">
<h3>Room List</h3>

<table>
<tr>
    <th>ID</th>
    <th>Number</th>
    <th>Type</th>
    <th>Price</th>
    <th>Capacity</th>
    <th>Status</th>
    <th>Actions</th>
</tr>

<% for(Room r : list) { %>
<tr>
    <td><%= r.getRoomId() %></td>
    <td><%= r.getRoomNumber() %></td>
    <td><%= r.getRoomType() %></td>
    <td>$<%= r.getPricePerNight() %></td>
    <td><%= r.getCapacity() %></td>
    <td><%= r.getStatus() %></td>
    <td>

        <!-- UPDATE -->
        <form action="${pageContext.request.contextPath}/staff/rooms" 
              method="post" style="display:inline;">
              
            <input type="hidden" name="id" value="<%= r.getRoomId() %>">
            <input type="hidden" name="roomNumber" value="<%= r.getRoomNumber() %>">
            <input type="hidden" name="roomType" value="<%= r.getRoomType() %>">
            <input type="hidden" name="price" value="<%= r.getPricePerNight() %>">
            <input type="hidden" name="capacity" value="<%= r.getCapacity() %>">
            <input type="hidden" name="status" value="AVAILABLE">

            <button type="submit" name="action" value="update" 
                    class="update-btn">
                Update
            </button>
        </form>

        <!-- DELETE -->
        <form action="${pageContext.request.contextPath}/staff/rooms" 
              method="post" style="display:inline;"
              onsubmit="return confirm('Are you sure?');">
              
            <input type="hidden" name="id" value="<%= r.getRoomId() %>">

            <button type="submit" name="action" value="delete" 
                    class="delete-btn">
                Delete
            </button>
        </form>

    </td>
</tr>
<% } %>

</table>
</div>

</body>
</html>