package ru.rostislav.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.rostislav.dto.ExchangeRateDto;
import ru.rostislav.dto.ExchangeResultDto;
import ru.rostislav.service.ExchangeService;

import java.util.List;

@Controller
public class ExchangeController {
    private final ExchangeService exchangeService;

    @Autowired
    public ExchangeController(ExchangeService exchangeService) {
        this.exchangeService = exchangeService;
    }

    @GetMapping("/exchange")
    @ResponseBody
    public ExchangeResultDto exchange(@RequestParam String from, @RequestParam String to, @RequestParam double amount) {
        return ExchangeResultDto.toDto(exchangeService.exchange(from, to, amount));
    }

}
