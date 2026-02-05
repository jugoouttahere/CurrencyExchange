package ru.rostislav.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.rostislav.dao.CurrencyDao;
import ru.rostislav.dao.ExchangeRateDao;
import ru.rostislav.exception.CurrencyNotFoundException;
import ru.rostislav.exception.ExchangeRateNotFoundException;
import ru.rostislav.model.Currency;
import ru.rostislav.model.ExchangeRate;
import ru.rostislav.model.ExchangeResult;

@Service
public class ExchangeService {
    private final CurrencyDao currencyDao;
    private final ExchangeRateDao exchangeRateDao;

    @Autowired
    public ExchangeService(CurrencyDao currencyDao, ExchangeRateDao exchangeRateDao) {
        this.currencyDao = currencyDao;
        this.exchangeRateDao = exchangeRateDao;
    }

    public ExchangeResult exchange(String fromCode, String toCode, double amount) {

        Currency from = currencyDao.findByCode(fromCode);
        if (from == null) {
            throw new CurrencyNotFoundException("Currency not found: " + fromCode);
        }

        Currency to = currencyDao.findByCode(toCode);
        if (to == null) {
            throw new CurrencyNotFoundException("Currency not found: " + toCode);
        }

        ExchangeRate direct = exchangeRateDao.findByCurrencyIds(from.getId(), to.getId());
        if (direct != null) {
            double result = amount * direct.getRate();
            return new ExchangeResult(from, to, direct.getRate(), amount, result);
        }

        ExchangeRate reverse = exchangeRateDao.findByCurrencyIds(to.getId(), from.getId());
        if (reverse != null) {
            double rate = 1 / reverse.getRate();
            double result = amount * rate;
            return new ExchangeResult(from, to, rate, amount, result);
        }

        Currency usd = currencyDao.findByCode("USD");
        if (usd == null) {
            throw new CurrencyNotFoundException("Base currency USD not found");
        }

        ExchangeRate usdToFrom = exchangeRateDao.findByCurrencyIds(usd.getId(), from.getId());
        ExchangeRate usdToTo = exchangeRateDao.findByCurrencyIds(usd.getId(), to.getId());
        if (usdToFrom != null && usdToTo != null) {
            double rate = usdToTo.getRate() / usdToFrom.getRate();
            double result = amount * rate;
            return new ExchangeResult(from, to, rate, amount, result);
        }

        throw new ExchangeRateNotFoundException(
                "Exchange rate not found for " + fromCode + " to " + toCode
        );
    }
}
