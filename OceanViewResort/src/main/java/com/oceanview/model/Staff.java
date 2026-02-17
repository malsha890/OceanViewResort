package com.oceanview.model;

public class Staff {

    private int staffId;
    private String fullName;
    private String email;
    private String password;
    private String role;

    public Staff() {}

    public Staff(int staffId, String fullName, String email, String role) {
        this.staffId = staffId;
        this.fullName = fullName;
        this.email = email;
        this.role = role;
    }

    public int getStaffId() { return staffId; }
    public void setStaffId(int staffId) { this.staffId = staffId; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
