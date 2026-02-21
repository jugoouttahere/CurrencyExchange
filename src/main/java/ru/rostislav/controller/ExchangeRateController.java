package ru.rostislav.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.rostislav.dto.ExchangeRateDto;
import ru.rostislav.exception.BadRequestException;
import ru.rostislav.service.ExchangeRateService;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class ExchangeRateController {
    private final ExchangeRateService exchangeRateService;

    @GetMapping("/exchangeRates")
    public List<ExchangeRateDto> getAll() {
        return exchangeRateService.findAll();
    }

    @GetMapping("/exchangeRate/{pair}")
    public ExchangeRateDto getPair(@PathVariable("pair") String pair) {
        return exchangeRateService.findByPair(pair);
    }

    @PostMapping("/exchangeRates")
    @ResponseStatus(HttpStatus.CREATED)
    public ExchangeRateDto create(@RequestParam String baseCurrencyCode, @RequestParam String targetCurrencyCode, @RequestParam double rate) {
        return exchangeRateService.create(baseCurrencyCode, targetCurrencyCode, rate);
    }

    @PatchMapping(value = "/exchangeRate/{pair}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ExchangeRateDto updateRate(@PathVariable String pair, @RequestParam("rate") Double rate) {
        return exchangeRateService.updateRate(pair, rate);
    }


}
