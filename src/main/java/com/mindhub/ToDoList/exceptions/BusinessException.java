package com.mindhub.ToDoList.exceptions;

import org.springframework.http.HttpStatus;

public class BusinessException extends RuntimeException {
    private static final HttpStatus DEFAULT_STATUS = HttpStatus.BAD_REQUEST;
    private final HttpStatus status;

    public BusinessException(String message) {
        super(message);
        this.status = DEFAULT_STATUS;
    }

    public BusinessException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
