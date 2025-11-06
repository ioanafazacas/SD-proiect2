package com.example.demo.dtos;

import java.util.Objects;
import java.util.UUID;

public class UserDTO {
    private UUID user_id;
    private String username;
    private String role;

    public UserDTO() {}

    public UserDTO(UUID user_id, String username, String role) {
        this.username = username;
        this.user_id = user_id;
        this.role = role;
    }

    public UUID getUser_id() { return user_id; }
    public void setUser_id(UUID user_id) { this.user_id = user_id; }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDTO that = (UserDTO) o;
        return Objects.equals(username, that.username) && Objects.equals(role, that.role);
    }
    @Override public int hashCode() { return Objects.hash(username, role); }
}
