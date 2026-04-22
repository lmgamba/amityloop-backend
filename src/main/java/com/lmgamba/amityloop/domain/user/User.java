package com.lmgamba.amityloop.domain.user;

import jakarta.persistence.*;
import java.time.LocalDateTime;

// Marks this class as a JPA entity — Hibernate will map it to a database table
@Entity
// Specifies the table name explicitly. Without this, Hibernate would use the class name "User"
// — which is a reserved word in PostgreSQL, so we use "users" to avoid conflicts
@Table(name = "users")
public class User {

    // Primary key of the table
    @Id
    // Auto-increments the ID on each insert (PostgreSQL uses SERIAL under the hood)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // nullable = false → NOT NULL in the database
    // unique = true → adds a UNIQUE constraint (no two users can share an email)
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, length = 60)
    private String password;

    @Column(nullable = false)
    private String name;

    // name = "created_at" maps the field to that exact column name in the table
    // updatable = false → Hibernate will never update this column after the first insert
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Runs automatically just before Hibernate inserts this entity into the database
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public LocalDateTime getCreatedAt() { return createdAt; }
}