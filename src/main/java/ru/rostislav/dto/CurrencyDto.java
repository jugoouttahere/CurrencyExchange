package ru.rostislav.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.rostislav.model.Currency;

@Getter
@AllArgsConstructor
public class CurrencyDto {
    private int id;
    private String code;
    private String name;
    private String sign;

    public static CurrencyDto toDto(Currency currency) {
        return new CurrencyDto(
                currency.getId(),
                currency.getCode(),
                currency.getFullName(),
                currency.getSign()
        );
    }

}
