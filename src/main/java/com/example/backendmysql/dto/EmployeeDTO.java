package com.example.backendmysql.dto;

import jakarta.validation.constraints.NotBlank;

public class EmployeeDTO {
    @NotBlank(message = "Name is required")
    private String name;
    @NotBlank(message = "Role is required")
    private String role;

    public EmployeeDTO() {}
    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRole(String role) {
        this.role = role;
    }
}