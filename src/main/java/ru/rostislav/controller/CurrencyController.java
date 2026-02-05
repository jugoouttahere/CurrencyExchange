package ru.rostislav.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.rostislav.dto.CurrencyDto;
import ru.rostislav.service.CurrencyService;

import java.util.List;

@Controller
public class CurrencyController {
    private final CurrencyService currencyService;

    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @GetMapping("/currencies")
    @ResponseBody
    public List<CurrencyDto> getCurrencies() {
        return currencyService.findAll();
    }

    @GetMapping("/currency/{code}")
    @ResponseBody
    public CurrencyDto getCurrency(@PathVariable String code) {
        return currencyService.findByCode(code);

    }

    @PostMapping("/currencies")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public CurrencyDto createCurrency(@RequestParam String name, @RequestParam String code, @RequestParam String sign) {
        return currencyService.addCurrency(name, code, sign);
    }

}
