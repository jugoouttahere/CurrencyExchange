package ru.rostislav.service;

import org.springframework.stereotype.Service;
import ru.rostislav.dao.CurrencyRepository;
import ru.rostislav.dao.ExchangeRateRepository;
import ru.rostislav.dto.CurrencyDto;
import ru.rostislav.dto.ExchangeRateDto;
import ru.rostislav.exception.AlreadyExistsException;
import ru.rostislav.exception.BadRequestException;
import ru.rostislav.exception.CurrencyNotFoundException;
import ru.rostislav.exception.ExchangeRateNotFoundException;
import ru.rostislav.model.Currency;
import ru.rostislav.model.ExchangeRate;

import java.util.ArrayList;
import java.util.List;


@Service
public class ExchangeRateService {
    private static final int CURRENCY_CODE_LENGTH = 3;
    private static final int PAIR_LENGTH = 6;

    private final ExchangeRateRepository exchangeRateRepository;
    private final CurrencyRepository currencyRepository;

    public ExchangeRateService(ExchangeRateRepository exchangeRateRepository, CurrencyRepository currencyRepository) {
        this.exchangeRateRepository = exchangeRateRepository;
        this.currencyRepository = currencyRepository;
    }

    public List<ExchangeRateDto> findAll() {
        List<ExchangeRateDto> result = new ArrayList<>();

        List<ExchangeRate> rates = exchangeRateRepository.findAll();
        for (ExchangeRate exchangeRate : rates) {

            Currency byBaseId = currencyRepository.findById(exchangeRate.getBaseCurrencyId());
            Currency byTargetId = currencyRepository.findById(exchangeRate.getTargetCurrencyId());

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

    public ExchangeRateDto findByPair(String pair) {
        if (pair == null || pair.isBlank()) {
            throw new BadRequestException("Currency pair is empty");
        }
        if (pair.length() != PAIR_LENGTH) {
            throw new BadRequestException("Currency pair must be 6 characters");
        }
        String baseCode = pair.substring(0, CURRENCY_CODE_LENGTH).toUpperCase();
        String targetCode = pair.substring(CURRENCY_CODE_LENGTH, PAIR_LENGTH).toUpperCase();

        Currency base = currencyRepository.findByCode(baseCode);
        Currency target = currencyRepository.findByCode(targetCode);

        if (base == null || target == null) {
            throw new CurrencyNotFoundException("Currency not found for pair");
        }

        ExchangeRate direct = exchangeRateRepository.findByCurrencyIds(base.getId(), target.getId());
        if (direct != null) {
            return new ExchangeRateDto(
                    direct.getId(),
                    CurrencyDto.toDto(base),
                    CurrencyDto.toDto(target),
                    direct.getRate()
            );
        }

        ExchangeRate reverse = exchangeRateRepository.findByCurrencyIds(target.getId(), base.getId());
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

        Currency base = currencyRepository.findByCode(baseCode.toUpperCase());
        Currency target = currencyRepository.findByCode(targetCode.toUpperCase());

        if (base == null || target == null) {
            throw new CurrencyNotFoundException("Currency not found");
        }

        ExchangeRate direct = exchangeRateRepository.findByCurrencyIds(base.getId(), target.getId());
        ExchangeRate reverse = exchangeRateRepository.findByCurrencyIds(target.getId(), base.getId());
        if (direct != null || reverse != null) {
            throw new AlreadyExistsException("Exchange rate already exists");
        }

        ExchangeRate saved = exchangeRateRepository.insert(
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

    public ExchangeRateDto updateRate(String pair, Double rate) {
        if (pair == null || pair.isBlank() || pair.length() != PAIR_LENGTH || rate <= 0) {
            throw new BadRequestException("Invalid request");
        }

        String baseCode = pair.substring(0, CURRENCY_CODE_LENGTH).toUpperCase();
        String targetCode = pair.substring(CURRENCY_CODE_LENGTH, PAIR_LENGTH).toUpperCase();

        Currency base = currencyRepository.findByCode(baseCode);
        Currency target = currencyRepository.findByCode(targetCode);
        if (base == null || target == null) {
            throw new CurrencyNotFoundException("Currency not found");
        }

        ExchangeRate direct = exchangeRateRepository.findByCurrencyIds(base.getId(), target.getId());
        ExchangeRate reverse = exchangeRateRepository.findByCurrencyIds(target.getId(), base.getId());
        if (direct == null && reverse == null) {
            throw new ExchangeRateNotFoundException("Exchange rate not found");
        }

        ExchangeRate updated;

        if (direct != null) {
            exchangeRateRepository.updateRate(base.getId(), target.getId(), rate);
            updated = new ExchangeRate(
                    direct.getId(),
                    base.getId(),
                    target.getId(),
                    rate
            );
        } else {
            Double reversedRate = 1 / rate;
            exchangeRateRepository.updateRate(target.getId(), base.getId(), reversedRate);
            updated = new ExchangeRate(
                    reverse.getId(),
                    base.getId(),
                    target.getId(),
                    rate
            );
        }

        return new ExchangeRateDto(
                updated.getId(),
                CurrencyDto.toDto(base),
                CurrencyDto.toDto(target),
                rate
        );
    }
}
