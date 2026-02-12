package ru.rostislav.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.rostislav.dao.CurrencyRepository;
import ru.rostislav.dto.CurrencyDto;
import ru.rostislav.exception.AlreadyExistsException;
import ru.rostislav.exception.BadRequestException;
import ru.rostislav.model.Currency;

import java.util.List;

@Service
public class CurrencyService {

    private final CurrencyRepository currencyRepository;

    @Autowired
    public CurrencyService(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    public List<CurrencyDto> findAll(int limit, int offset) {
        return currencyRepository.findAll(limit, offset)
                .stream()
                .map(CurrencyDto::toDto)
                .toList();
    }

    public CurrencyDto findByCode(String code) {
        if (code == null || code.isBlank()) {
            throw new BadRequestException("Currency code is empty");
        }

        Currency currency = currencyRepository.findByCode(code);

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

        if (currencyRepository.findByCode(code) != null) {
            throw new AlreadyExistsException("Currency with this code already exist");
        }

        Currency saved = currencyRepository.insert(code, name, sign);
        return CurrencyDto.toDto(saved);
    }


}
