package ru.ulstu.library.exception;

public class InvalidBookStateException extends RuntimeException {

    public InvalidBookStateException(String message) {
        super(message);
    }
}
