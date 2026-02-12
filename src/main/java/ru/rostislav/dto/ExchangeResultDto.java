package ru.rostislav.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.rostislav.model.ExchangeResult;

@Getter
@AllArgsConstructor
public class ExchangeResultDto {
    private CurrencyDto baseCurrency;
    private CurrencyDto targetCurrency;
    private double rate;
    private double amount;
    private double result;
}
