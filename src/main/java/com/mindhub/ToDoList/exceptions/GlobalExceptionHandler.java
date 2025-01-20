package com.mindhub.ToDoList.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handlerUserNotFoundException(UserNotFoundException ex){
        return new ResponseEntity<>(ex.getMessage(),ex.getStatus());
    }


    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<Object> handlerTaskNotFoundException(TaskNotFoundException ex){
        return new ResponseEntity<>(ex.getMessage(),ex.getStatus());
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<Object> handlerConflictException(ConflictException ex){
        return new ResponseEntity<>(ex.getMessage(),ex.getStatus());
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Object> handlerBusinessException(BusinessException ex){
        return new ResponseEntity<>(ex.getMessage(),ex.getStatus());
    }
}
