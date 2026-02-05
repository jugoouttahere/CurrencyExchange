package ru.rostislav.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.rostislav.model.ExchangeResult;

@Getter
@AllArgsConstructor
public class ExchangeResultDto {
    private String from;
    private String to;
    private double rate;
    private double amount;
    private double result;

    public static ExchangeResultDto toDto(ExchangeResult exchangeResult) {
        return new ExchangeResultDto(
                exchangeResult.getFrom().getCode(),
                exchangeResult.getTo().getCode(),
                exchangeResult.getRate(),
                exchangeResult.getAmount(),
                exchangeResult.getConvertedAmount()
        );
    }

}
