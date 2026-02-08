package ru.rostislav.exception;

public class ExchangeRateAlreadyExistsException extends RuntimeException {
    public ExchangeRateAlreadyExistsException(String message) {
        super(message);
    }
}
