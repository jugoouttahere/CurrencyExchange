package ru.rostislav.exception;

public class CurrencyAlreadyExistsException extends RuntimeException {
    public CurrencyAlreadyExistsException(String message) {
        super(message);
    }
}
