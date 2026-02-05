package ru.rostislav.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.rostislav.dto.ErrorResponse;
import ru.rostislav.exception.BadRequestException;
import ru.rostislav.exception.CurrencyAlreadyExistsException;
import ru.rostislav.exception.CurrencyNotFoundException;
import ru.rostislav.exception.ExchangeRateNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CurrencyNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleCurrencyNotFound(CurrencyNotFoundException ex) {
        return new ErrorResponse(ex.getMessage());
    }

    @ExceptionHandler(ExchangeRateNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleExchangeRateNotFound(ExchangeRateNotFoundException ex) {
        return new ErrorResponse(ex.getMessage());
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequest(BadRequestException ex) {
        return new ErrorResponse(ex.getMessage());
    }

    @ExceptionHandler(CurrencyAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleAlreadyExists(CurrencyAlreadyExistsException ex) {
        return new ErrorResponse(ex.getMessage());
    }

}
