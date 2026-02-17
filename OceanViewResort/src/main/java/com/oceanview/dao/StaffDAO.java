package com.oceanview.dao;

import com.oceanview.model.Staff;
import com.oceanview.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class StaffDAO {

    public Staff login(String email, String password) {

        Staff staff = null;

        try (Connection con = DBConnection.getConnection()) {

            String sql = "SELECT * FROM staff WHERE email=? AND password=? AND status='ACTIVE'";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                staff = new Staff(
                        rs.getInt("staff_id"),
                        rs.getString("full_name"),
                        rs.getString("email"),
                        rs.getString("role")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return staff;
    }
}
