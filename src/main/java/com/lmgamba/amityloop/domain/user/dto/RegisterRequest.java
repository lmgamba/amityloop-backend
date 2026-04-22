package com.lmgamba.amityloop.domain.user.dto;

// DTO (Data Transfer Object) — represents the data expected in the request body for registration
// This class never touches the database — it only carries data from the HTTP request to the service
public class RegisterRequest {

    // These fields map directly to the JSON body the client sends:
    // { "name": "Laura", "email": "laura@email.com", "password": "12345" }
    private String name;
    private String email;
    private String password;

    // Default constructor required for Jackson to deserialize JSON into this object
    public RegisterRequest() {}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}