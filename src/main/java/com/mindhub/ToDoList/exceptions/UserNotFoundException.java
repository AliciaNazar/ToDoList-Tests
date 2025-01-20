package com.mindhub.ToDoList.exceptions;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends RuntimeException{

    private final HttpStatus status;

    public UserNotFoundException() {
        super("User not found.");
        this.status = HttpStatus.NOT_FOUND;
    }

    public HttpStatus getStatus(){
        return status;
    }
}
