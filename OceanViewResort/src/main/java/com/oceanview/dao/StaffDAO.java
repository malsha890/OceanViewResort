
package com.oceanview.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.oceanview.model.Staff;
import com.oceanview.util.DBConnection;

public class StaffDAO {

    private Connection conn;

    public StaffDAO() throws Exception {
        conn = DBConnection.getConnection();
    }
 // LOGIN METHOD WITH BCrypt
    public Staff login(String email, String password) throws Exception {

        String sql = "SELECT * FROM staff WHERE email=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, email);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {

            String storedHash = rs.getString("password");

            // Check hashed password
            if (com.oceanview.util.PasswordUtil.checkPassword(password, storedHash)) {

                Staff staff = new Staff();
                staff.setStaffId(rs.getInt("staff_id"));
                staff.setFullName(rs.getString("full_name"));
                staff.setEmail(rs.getString("email"));
                staff.setRole(rs.getString("role"));

                return staff;
            }
        }

        return null;
    }

    // GET ALL STAFF
    public List<Staff> getAllStaff() throws Exception {

        List<Staff> list = new ArrayList<>();

        String sql = "SELECT * FROM staff";

        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Staff staff = new Staff();
            staff.setStaffId(rs.getInt("staff_id"));
            staff.setFullName(rs.getString("full_name"));
            staff.setEmail(rs.getString("email"));
            staff.setRole(rs.getString("role"));
            list.add(staff);
        }

        return list;
    }

    // ADD STAFF
    public void addStaff(Staff staff) throws Exception {

        String sql = "INSERT INTO staff (full_name, email, password, role) VALUES (?, ?, ?, ?)";

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, staff.getFullName());
        ps.setString(2, staff.getEmail());
        ps.setString(3, staff.getPassword());
        ps.setString(4, staff.getRole());

        ps.executeUpdate();
    }
 // SEARCH STAFF
    public List<Staff> searchStaff(String keyword) throws Exception {

        List<Staff> list = new ArrayList<>();

        String sql = "SELECT * FROM staff WHERE "
                   + "CAST(staff_id AS CHAR) LIKE ? "
                   + "OR full_name LIKE ? "
                   + "OR email LIKE ?";

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, "%" + keyword + "%");
        ps.setString(2, "%" + keyword + "%");
        ps.setString(3, "%" + keyword + "%");

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Staff staff = new Staff();
            staff.setStaffId(rs.getInt("staff_id"));
            staff.setFullName(rs.getString("full_name"));
            staff.setEmail(rs.getString("email"));
            staff.setRole(rs.getString("role"));
            list.add(staff);
        }

        return list;
    }



    // GET STAFF BY ID
    public Staff getStaffById(int id) throws Exception {

        String sql = "SELECT * FROM staff WHERE staff_id=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            Staff staff = new Staff();
            staff.setStaffId(rs.getInt("staff_id"));
            staff.setFullName(rs.getString("full_name"));
            staff.setEmail(rs.getString("email"));
            staff.setRole(rs.getString("role"));
            return staff;
        }

        return null;
    }


    // UPDATE STAFF
    public void updateStaff(Staff staff) throws Exception {

        String sql = "UPDATE staff SET full_name=?, email=?, role=? WHERE staff_id=?";

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, staff.getFullName());
        ps.setString(2, staff.getEmail());
        ps.setString(3, staff.getRole());
        ps.setInt(4, staff.getStaffId());

        ps.executeUpdate();
    }

    // DELETE STAFF
    public void deleteStaff(int id) throws Exception {

        String sql = "DELETE FROM staff WHERE staff_id=?";

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);
        ps.executeUpdate();
    }

	
	

	
    
}
