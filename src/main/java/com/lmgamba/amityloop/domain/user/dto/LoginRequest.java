package com.lmgamba.amityloop.domain.user.dto;

// DTO for login — only needs email and password, not name
public class LoginRequest {

    private String email;
    private String password;

    public LoginRequest() {}

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}