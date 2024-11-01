package com.example.api.models;

import jakarta.validation.constraints.NotEmpty;

public class LoginDto {
    @NotEmpty
    private String username;

    @NotEmpty
    private String password;

    // Getters
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    // Setters
    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
