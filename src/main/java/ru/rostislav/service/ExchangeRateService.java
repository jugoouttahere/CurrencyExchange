package ru.rostislav.service;

import org.springframework.stereotype.Service;
import ru.rostislav.dao.CurrencyDao;
import ru.rostislav.dao.ExchangeRateDao;
import ru.rostislav.dto.CurrencyDto;
import ru.rostislav.dto.ExchangeRateDto;
import ru.rostislav.exception.BadRequestException;
import ru.rostislav.exception.CurrencyNotFoundException;
import ru.rostislav.exception.ExchangeRateAlreadyExistsException;
import ru.rostislav.exception.ExchangeRateNotFoundException;
import ru.rostislav.model.Currency;
import ru.rostislav.model.ExchangeRate;

import java.util.ArrayList;
import java.util.List;


@Service
public class ExchangeRateService {
    private final ExchangeRateDao exchangeRateDao;
    private final CurrencyDao currencyDao;

    public ExchangeRateService(ExchangeRateDao exchangeRateDao, CurrencyDao currencyDao) {
        this.exchangeRateDao = exchangeRateDao;
        this.currencyDao = currencyDao;
    }

    public List<ExchangeRateDto> findAll() {
        List<ExchangeRateDto> result = new ArrayList<>();

        List<ExchangeRate> rates = exchangeRateDao.findAll();
        for (ExchangeRate exchangeRate : rates) {

            Currency byBaseId = currencyDao.findById(exchangeRate.getBaseCurrencyId());
            Currency byTargetId = currencyDao.findById(exchangeRate.getTargetCurrencyId());

            if (byTargetId == null || byBaseId == null) {
                throw new CurrencyNotFoundException("Currency not found for exchange rate");
            }

            CurrencyDto baseCurrency = CurrencyDto.toDto(byBaseId);
            CurrencyDto targetCurrency = CurrencyDto.toDto(byTargetId);

            ExchangeRateDto dto = new ExchangeRateDto(
                    exchangeRate.getId(),
                    baseCurrency,
                    targetCurrency,
                    exchangeRate.getRate()
            );

            result.add(dto);
        }

        return result;
    }

    public ExchangeRateDto findByPair(String baseCode, String targetCode) {
        if (baseCode == null || baseCode.isBlank() || targetCode == null || targetCode.isBlank()) {
            throw new BadRequestException("Currency codes must not be empty");
        }

        Currency base = currencyDao.findByCode(baseCode);
        Currency target = currencyDao.findByCode(targetCode);

        if (base == null || target == null) {
            throw new CurrencyNotFoundException("Currency not found for pair");
        }

        ExchangeRate direct = exchangeRateDao.findByCurrencyIds(base.getId(), target.getId());
        if (direct != null) {
            return new ExchangeRateDto(
                    direct.getId(),
                    CurrencyDto.toDto(base),
                    CurrencyDto.toDto(target),
                    direct.getRate()
            );
        }

        ExchangeRate reverse = exchangeRateDao.findByCurrencyIds(target.getId(), base.getId());
        if (reverse != null) {
            return new ExchangeRateDto(
                    reverse.getId(),
                    CurrencyDto.toDto(base),
                    CurrencyDto.toDto(target),
                    1 / reverse.getRate()
            );
        }

        throw new ExchangeRateNotFoundException("Exchange rate not found for pair");
    }

    public ExchangeRateDto create(String baseCode, String targetCode, double rate) {
        if (baseCode == null || baseCode.isBlank() || targetCode == null || targetCode.isBlank() || rate <= 0) {
            throw new BadRequestException("Invalid request");
        }

        Currency base = currencyDao.findByCode(baseCode);
        Currency target = currencyDao.findByCode(targetCode);

        if (base == null || target == null) {
            throw new CurrencyNotFoundException("Currency not found");
        }

        ExchangeRate direct = exchangeRateDao.findByCurrencyIds(base.getId(), target.getId());
        ExchangeRate reverse = exchangeRateDao.findByCurrencyIds(target.getId(), base.getId());
        if (direct != null || reverse != null) {
            throw new ExchangeRateAlreadyExistsException("Exchange rate already exists");
        }

        ExchangeRate saved = exchangeRateDao.insert(
                base.getId(),
                target.getId(),
                rate
        );

        return new ExchangeRateDto(
                saved.getId(),
                CurrencyDto.toDto(base),
                CurrencyDto.toDto(target),
                saved.getRate()
        );
    }
}
