package com.mindhub.ToDoList.dtos.taskDTOs;

import com.mindhub.ToDoList.dtos.userDTOs.UserIdDTO;
import com.mindhub.ToDoList.models.EntityUser;
import com.mindhub.ToDoList.models.Task;
import com.mindhub.ToDoList.models.enums.TaskStatus;

public class TaskDTORequest {

    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private UserIdDTO user;


    public UserIdDTO getUser() {
        return user;
    }

    public void setUser(UserIdDTO user) {
        this.user = user;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }



    public static Task toEntity(TaskDTORequest taskDTORequest, EntityUser user){
        Task task = new Task();
        task.setStatus(taskDTORequest.getStatus());
        task.setDescription(taskDTORequest.getDescription());
        task.setTitle(taskDTORequest.getTitle());
        task.setUser(user);
        return task;
    }
}
