package com.example.inticivi.dto;

import jakarta.validation.constraints.NotBlank;

public class RoleUpdateRequest {

    @NotBlank
    private String role;

    public RoleUpdateRequest() {
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
