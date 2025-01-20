package com.mindhub.ToDoList.exceptions;

import org.springframework.http.HttpStatus;

public class TaskNotFoundException extends RuntimeException{


   private final HttpStatus status;

    public TaskNotFoundException() {
        super("Task not found.");
        this.status = HttpStatus.NOT_FOUND;
    }

    public HttpStatus getStatus(){
        return status;
    }
}
