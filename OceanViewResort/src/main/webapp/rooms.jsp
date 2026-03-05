<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.*, com.oceanview.model.Room" %>

<%
List<Room> list = (List<Room>) request.getAttribute("list");
if (list == null) {
    list = new ArrayList<>();
}
%>
<%@ page import="java.text.DecimalFormat" %>

<%
DecimalFormat df = new DecimalFormat("#,###.00");
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

    <!-- Hidden fields -->
    <input type="hidden" name="action" value="add" id="formAction">
    <input type="hidden" name="id" id="roomId">

    Room Number:
    <input type="text" name="roomNumber" id="roomNumber">

    Type:
    <select name="roomType" id="roomType">
        <option value="STANDARD">STANDARD</option>
        <option value="SUITE">SUITE</option>
    </select>

    Price:
    <input type="number" name="price" id="price">

    Capacity:
    <input type="number" name="capacity" id="capacity">

    Status:
    <select name="status" id="status">
        <option value="ACTIVE">ACTIVE</option>
        <option value="MAINTENANCE">MAINTENANCE</option>
        <option value="INACTIVE">INACTIVE</option>
    </select>

    <button type="submit" id="submitBtn">Add Room</button>
</form>
</div>

<div class="card">
<h3>Room List</h3>
<form action="${pageContext.request.contextPath}/staff/rooms" method="get" style="margin-bottom:15px;">

    <input type="hidden" name="action" value="search">

    <input type="text" name="keyword" placeholder="Search by number, type or status">

    <button type="submit">Search</button>

    <a href="${pageContext.request.contextPath}/staff/rooms">
        <button type="button">Reset</button>
    </a>

</form>
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
    <td>Rs.<%= r.getPricePerNight() %></td>
    <td><%= r.getCapacity() %></td>
    <td><%= r.getStatus() %></td>
    <td>

        <!-- UPDATE -->
      

         <button type="button"
    onclick="editRoom(
        '<%= r.getRoomId() %>',
        '<%= r.getRoomNumber() %>',
        '<%= r.getRoomType() %>',
        '<%= r.getPricePerNight() %>',
        '<%= r.getCapacity() %>',
        '<%= r.getStatus() %>'
    )"
    class="update-btn">
    Update
</button>
        

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
<script>
function editRoom(id, number, type, price, capacity, status) {

    document.getElementById("roomId").value = id;
    document.getElementById("roomNumber").value = number;
    document.getElementById("roomType").value = type;
    document.getElementById("price").value = price;
    document.getElementById("capacity").value = capacity;
    document.getElementById("status").value = status;

    // Change form mode to UPDATE
    document.getElementById("formAction").value = "update";

    // Change button text
    document.getElementById("submitBtn").innerText = "Update Room";

    // Scroll to top
    window.scrollTo(0, 0);
}
</script>
</body>
</html>