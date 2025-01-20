package com.mindhub.ToDoList.dtos.userDTOs;

import com.mindhub.ToDoList.dtos.taskDTOs.TaskDTO;
import com.mindhub.ToDoList.models.EntityUser;
import com.mindhub.ToDoList.models.enums.RoleType;

import java.util.List;

public class UserDTO {

    private Long id;
    private String username;
    private String email;
    private List<TaskDTO> tasks;
    private RoleType roleType;

    public UserDTO(){}

    public UserDTO(EntityUser user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.tasks = user
                .getTasks()
                .stream()
                .map(task -> new TaskDTO(task))
                .toList();
        this.roleType = user.getRoleType();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<TaskDTO> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskDTO> tasks) {
        this.tasks = tasks;
    }

    public RoleType getRoleType() {
        return roleType;
    }

    public void setRoleType(RoleType roleType) {
        this.roleType = roleType;
    }
}

