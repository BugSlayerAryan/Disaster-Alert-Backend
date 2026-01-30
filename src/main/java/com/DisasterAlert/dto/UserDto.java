package com.DisasterAlert.dto;

public class UserDto {

    private String clerkUserId;
    private String name;
    private String email;
    private String phone;
    private String location;
    private String role; // 🔹 Add this field

    // 🔹 Getters and Setters
    public String getClerkUserId() {
        return clerkUserId;
    }

    public void setClerkUserId(String clerkUserId) {
        this.clerkUserId = clerkUserId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getRole() {  // 🔹 Getter for role
        return role;
    }

    public void setRole(String role) { // 🔹 Setter for role
        this.role = role;
    }
}
