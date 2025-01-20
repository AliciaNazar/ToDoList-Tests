package com.mindhub.ToDoList.dtos.authDTOs;

import com.mindhub.ToDoList.models.enums.RoleType;

public class RegisterDTO {
    private String username;
    private String password;
    private String email;
    private RoleType roleType;


    public RegisterDTO(String username, String password, String email, RoleType roleType) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.roleType = roleType;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public RoleType getRoleType() {
        return roleType;
    }

    public void setRoleType(RoleType roleType) {
        this.roleType = roleType;
    }
}
