package com.application.bookstore.dto;

public class AppUserDto {

    private int id;
    private String username;
    private String roles;

    // Constructors
    public AppUserDto() {}

    public AppUserDto(int id, String username, String roles) {
        this.id = id;
        this.username = username;
        this.roles = roles;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }
}
