<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ page import="java.util.*" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<title>Insert title here</title>
</head>
<body>
<h2>Hotel Reports</h2>

<table border="1" cellpadding="10">

<tr>
<td>Total Reservations</td>
<td>${totalReservations}</td>
</tr>

<tr>
<td>Total Revenue</td>
<td>Rs. ${totalRevenue}</td>
</tr>

<tr>
<td>Active Rooms</td>
<td>${activeRooms}</td>
</tr>

<tr>
<td>Cancelled Reservations</td>
<td>${cancelledReservations}</td>
</tr>

</table>

<h2>Monthly Reservations</h2>

<canvas id="reservationChart" width="400" height="200"></canvas>

<h2>Monthly Revenue</h2>

<canvas id="revenueChart" width="400" height="200"></canvas>
<script>

const reservationLabels = [
<%
Map<String,Integer> reservations =
(Map<String,Integer>)request.getAttribute("monthlyReservations");

if(reservations != null){
for(String key : reservations.keySet()){
%>
"<%=key%>",
<% }} %>
];

const reservationData = [
<%
if(reservations != null){
for(Integer value : reservations.values()){
%>
<%=value%>,
<% }} %>
];

const revenueLabels = [
<%
Map<String,Double> revenue =
(Map<String,Double>)request.getAttribute("monthlyRevenue");

if(revenue != null){
for(String key : revenue.keySet()){
%>
"<%=key%>",
<% }} %>
];

const revenueData = [
<%
if(revenue != null){
for(Double value : revenue.values()){
%>
<%=value%>,
<% }} %>
];

new Chart(document.getElementById("reservationChart"), {
	type: 'bar',
	data: {
	labels: reservationLabels,
	datasets: [{
	label: 'Monthly Reservations',
	data: reservationData,
	backgroundColor: 'rgba(54, 162, 235, 0.6)',
	borderColor: 'rgba(54, 162, 235, 1)',
	borderWidth: 1
	}]
	},
	options: {
	scales: {
	y: {
	beginAtZero: true
	}
	}
	}
	});

new Chart(document.getElementById("revenueChart"), {
	type: 'line',
	data: {
	labels: revenueLabels,
	datasets: [{
	label: 'Revenue (Rs)',
	data: revenueData
	}]
	}
});

</script>
</body>
</html>