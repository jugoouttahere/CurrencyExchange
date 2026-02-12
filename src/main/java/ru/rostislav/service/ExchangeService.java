package ru.rostislav.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.rostislav.dao.CurrencyRepository;
import ru.rostislav.dao.ExchangeRateRepository;
import ru.rostislav.exception.ExchangeRateNotFoundException;
import ru.rostislav.model.Currency;
import ru.rostislav.model.ExchangeRate;
import ru.rostislav.model.ExchangeResult;

@RequiredArgsConstructor
@Service
public class ExchangeService {
    private static final String USD_CURRENCY_CODE = "USD";

    private final CurrencyRepository currencyRepository;
    private final ExchangeRateRepository exchangeRateRepository;

    public ExchangeResult exchange(String baseCode, String targetCode, double amount) {

        Currency base = findCurrency(baseCode.toUpperCase());
        Currency target = findCurrency(targetCode.toUpperCase());

        double rate = resolveRate(base, target);

        return buildResult(base, target, rate, amount);
    }

    private Currency findCurrency(String code) {
        return currencyRepository.findByCode(code);
    }

    private double resolveRate(Currency base, Currency target) {
        ExchangeRate direct = findDirectRate(base, target);
        if (direct != null) {
            return direct.getRate();
        }

        ExchangeRate reverse = findDirectRate(target, base);
        if (reverse != null) {
            return 1 / reverse.getRate();
        }

        return resolveCrossRate(base, target);
    }

    private ExchangeRate findDirectRate(Currency base, Currency target) {
        return exchangeRateRepository.findByCurrencyIds(base.getId(), target.getId());
    }

    private double resolveCrossRate(Currency base, Currency target) {
        Currency usd = currencyRepository.findByCode(USD_CURRENCY_CODE);

        ExchangeRate usdToFrom = findDirectRate(usd, base);
        ExchangeRate usdToTo = findDirectRate(usd, target);

        if (usdToFrom != null && usdToTo != null) {
            return usdToTo.getRate() / usdToFrom.getRate();
        }

        throw new ExchangeRateNotFoundException(
                "Exchange rate not found for " + base.getCode() + " to " + target.getCode()
        );
    }

    private ExchangeResult buildResult(Currency base, Currency target, double rate, double amount) {
        double convertedResult = amount * rate;
        return new ExchangeResult(base, target, rate, amount, convertedResult);
    }
}
