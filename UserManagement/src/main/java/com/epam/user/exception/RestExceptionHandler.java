package com.epam.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestControllerAdvice
public class RestExceptionHandler {
    private ExceptionResponse buildExceptionResponse(HttpStatus status, String errorMessage) {
        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.setTimeStamp(new Date().toString());
        if(status.equals(HttpStatus.INTERNAL_SERVER_ERROR))
            exceptionResponse.setErrorMessage(errorMessage);
        else
            exceptionResponse.setDeveloperMessage(errorMessage);
        exceptionResponse.setStatus(status.name());
        return exceptionResponse;
    }
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException exception, WebRequest request) {

        List<String> errors = new ArrayList<>();
        exception.getBindingResult().getAllErrors().forEach(error -> errors.add(error.getDefaultMessage()));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(buildExceptionResponse(HttpStatus.BAD_REQUEST, errors.toString()));

    }
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ExceptionResponse> handleException(Exception exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(buildExceptionResponse( HttpStatus.BAD_REQUEST, exception.getMessage()));
    }
    /*@ExceptionHandler(value = ResourceNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleResourceNotFoundException(ResourceNotFoundException exception,WebRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(buildExceptionResponse(request, HttpStatus.NOT_FOUND, exception.getMessage()));
    }
    @ExceptionHandler(value = DuplicateUserNameException.class)
    public ResponseEntity<ExceptionResponse> handleDuplicateUserNameException(DuplicateUserNameException exception, WebRequest request) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(buildExceptionResponse(request, HttpStatus.CONFLICT, exception.getMessage()));
    }*/
}
