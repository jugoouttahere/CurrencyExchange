package ru.rostislav.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.rostislav.dto.CurrencyDto;
import ru.rostislav.service.CurrencyService;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class CurrencyController {
    private final CurrencyService currencyService;

    @GetMapping("/currencies")
    public List<CurrencyDto> getCurrencies(@RequestParam(defaultValue = "50") int limit, @RequestParam(defaultValue = "0") int offset) {
        return currencyService.findAll(limit, offset);
    }

    @GetMapping("/currency/{code}")
    public CurrencyDto getCurrency(@PathVariable String code) {
        return currencyService.findByCode(code);

    }

    @PostMapping("/currencies")
    @ResponseStatus(HttpStatus.CREATED)
    public CurrencyDto createCurrency(@RequestParam String name, @RequestParam String code, @RequestParam String sign) {
        return currencyService.addCurrency(name, code, sign);
    }

}
