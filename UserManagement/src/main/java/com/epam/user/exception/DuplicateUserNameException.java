package com.epam.user.exception;

public class DuplicateUserNameException extends Exception {
    private static final long serialVersionUID = 1L;
    public DuplicateUserNameException(String message) {
        super(message);
    }
}
