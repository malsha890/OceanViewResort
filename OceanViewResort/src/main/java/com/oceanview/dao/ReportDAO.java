package com.oceanview.dao;

import com.oceanview.util.DBConnection;
import java.sql.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class ReportDAO {

public int getTotalReservations() {

String sql = "SELECT COUNT(*) FROM reservations WHERE status='CONFIRMED'";

try(Connection con = DBConnection.getConnection();
PreparedStatement ps = con.prepareStatement(sql);
ResultSet rs = ps.executeQuery()){

if(rs.next()){
return rs.getInt(1);
}

}catch(Exception e){
e.printStackTrace();
}

return 0;
}

public double getTotalRevenue(){

String sql = "SELECT SUM(total_amount) FROM reservations WHERE status='CONFIRMED'";

try(Connection con = DBConnection.getConnection();
PreparedStatement ps = con.prepareStatement(sql);
ResultSet rs = ps.executeQuery()){

if(rs.next()){
return rs.getDouble(1);
}

}catch(Exception e){
e.printStackTrace();
}

return 0;
}

public int getActiveRooms(){

String sql = "SELECT COUNT(*) FROM rooms WHERE status='ACTIVE'";

try(Connection con = DBConnection.getConnection();
PreparedStatement ps = con.prepareStatement(sql);
ResultSet rs = ps.executeQuery()){

if(rs.next()){
return rs.getInt(1);
}

}catch(Exception e){
e.printStackTrace();
}

return 0;
}

public int getCancelledReservations(){

String sql = "SELECT COUNT(*) FROM reservations WHERE status='CANCELLED'";

try(Connection con = DBConnection.getConnection();
PreparedStatement ps = con.prepareStatement(sql);
ResultSet rs = ps.executeQuery()){

if(rs.next()){
return rs.getInt(1);
}

}catch(Exception e){
e.printStackTrace();
}

return 0;
}

public Map<String, Integer> getMonthlyReservations() {

Map<String, Integer> data = new LinkedHashMap<>();

String sql = "SELECT MONTH(check_in) as month, COUNT(*) as total " +
             "FROM reservations WHERE status='CONFIRMED' " +
             "GROUP BY MONTH(check_in)";

try(Connection con = DBConnection.getConnection();
PreparedStatement ps = con.prepareStatement(sql);
ResultSet rs = ps.executeQuery()){

while(rs.next()){

data.put("Month " + rs.getInt("month"), rs.getInt("total"));

}

}catch(Exception e){
e.printStackTrace();
}

return data;
}

public Map<String, Double> getMonthlyRevenue(){

Map<String, Double> data = new LinkedHashMap<>();

String sql = "SELECT MONTH(check_in) as month, SUM(total_amount) as revenue " +
             "FROM reservations WHERE status='CONFIRMED' " +
             "GROUP BY MONTH(check_in)";

try(Connection con = DBConnection.getConnection();
PreparedStatement ps = con.prepareStatement(sql);
ResultSet rs = ps.executeQuery()){

while(rs.next()){

data.put("Month " + rs.getInt("month"), rs.getDouble("revenue"));

}

}catch(Exception e){
e.printStackTrace();
}

return data;
}


















}