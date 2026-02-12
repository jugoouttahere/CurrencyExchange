package ru.rostislav.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.rostislav.dto.ExchangeResultDto;
import ru.rostislav.service.ExchangeService;

@RequiredArgsConstructor
@RestController
public class ExchangeController {
    private final ExchangeService exchangeService;

    @GetMapping("/exchange")
    public ExchangeResultDto exchange(@RequestParam String from, @RequestParam String to, @RequestParam double amount) {
        return exchangeService.exchange(from, to, amount).toDto();
    }

}
