package ru.rostislav.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.rostislav.dto.ExchangeRateDto;
import ru.rostislav.exception.BadRequestException;
import ru.rostislav.service.ExchangeRateService;

import java.util.List;

@Controller
public class ExchangeRateController {
    private final ExchangeRateService exchangeRateService;

    public ExchangeRateController(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }

    @GetMapping("/exchangeRates")
    @ResponseBody
    public List<ExchangeRateDto> getAll() {
        return exchangeRateService.findAll();
    }

    @GetMapping("/exchangeRate/{pair}")
    @ResponseBody
    public ExchangeRateDto getPair(@PathVariable("pair") String pair) {
        if (pair == null || pair.isBlank()) {
            throw new BadRequestException("Currency pair is empty");
        }
        if (pair.length() != 6) {
            throw new BadRequestException("Currency pair must be 6 characters");
        }
        String baseCode = pair.substring(0, 3);
        String targetCode = pair.substring(3, 6);

        return exchangeRateService.findByPair(baseCode, targetCode);
    }

    @PostMapping("/exchangeRates")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ExchangeRateDto create(@RequestParam String baseCurrencyCode, @RequestParam String targetCurrencyCode, @RequestParam double rate) {
        return exchangeRateService.create(baseCurrencyCode, targetCurrencyCode, rate);
    }

}
