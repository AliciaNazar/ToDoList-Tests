package com.mindhub.ToDoList.exceptions;

import org.springframework.http.HttpStatus;

public class ConflictException extends RuntimeException{


  private final HttpStatus status;

  public ConflictException() {
    super("Username already exists.");
    this.status = HttpStatus.CONFLICT;
  }

  public HttpStatus getStatus(){
    return status;
  }
}