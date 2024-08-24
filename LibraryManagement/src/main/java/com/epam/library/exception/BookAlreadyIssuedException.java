package com.epam.library.exception;

public class BookAlreadyIssuedException extends Exception{
    private static final long serialVersionUID = 1L;
    public BookAlreadyIssuedException(String message) {
        super(message);
    }
}
