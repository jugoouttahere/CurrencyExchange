package ru.rostislav.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.rostislav.dto.CurrencyDto;
import ru.rostislav.dto.ExchangeResultDto;

@Getter
@AllArgsConstructor
public class ExchangeResult {

    private Currency from;
    private Currency to;
    private double rate;
    private double amount;
    private double convertedAmount;

    public ExchangeResultDto toDto() {
        return new ExchangeResultDto(
                CurrencyDto.toDto(from),
                CurrencyDto.toDto(to),
                rate,
                amount,
                convertedAmount
        );
    }
}
