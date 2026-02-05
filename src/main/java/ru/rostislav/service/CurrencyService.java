package ru.rostislav.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.rostislav.dao.CurrencyDao;
import ru.rostislav.dto.CurrencyDto;
import ru.rostislav.exception.BadRequestException;
import ru.rostislav.exception.CurrencyAlreadyExistsException;
import ru.rostislav.exception.CurrencyNotFoundException;
import ru.rostislav.model.Currency;

import java.util.List;

@Service
public class CurrencyService {

    private final CurrencyDao currencyDao;

    @Autowired
    public CurrencyService(CurrencyDao currencyDao) {
        this.currencyDao = currencyDao;
    }

    public List<CurrencyDto> findAll() {
        return currencyDao.findAll().stream()
                .map(CurrencyDto::toDto)
                .toList();
    }

    public CurrencyDto findByCode(String code) {
        if (code == null || code.isBlank()) {
            throw new BadRequestException("Currency code is empty");
        }

        Currency currency = currencyDao.findByCode(code);

        if (currency == null) {
            throw new CurrencyNotFoundException("Currency not found: " + code);
        }

        return CurrencyDto.toDto(currency);
    }

    public CurrencyDto addCurrency(String name, String code, String sign) {
        if (name == null || name.isBlank()) {
            throw new BadRequestException("Name is empty");
        }
        if (code == null || code.isBlank()) {
            throw new BadRequestException("Code is empty");
        }
        if (sign == null || sign.isBlank()) {
            throw new BadRequestException("Sign is empty");
        }

        if (currencyDao.findByCode(code) != null) {
            throw new CurrencyAlreadyExistsException("Currency with this code already exist");
        }

        Currency saved = currencyDao.insert(code, name, sign);
        return CurrencyDto.toDto(saved);
    }


}
