package ru.rostislav.model;

public class ExchangeResult {

    private Currency from;
    private Currency to;
    private double rate;
    private double amount;
    private double convertedAmount;

    public ExchangeResult(Currency from, Currency to, double rate, double amount, double convertedAmount) {
        this.from = from;
        this.to = to;
        this.rate = rate;
        this.amount = amount;
        this.convertedAmount = convertedAmount;
    }

    public Currency getFrom() {
        return from;
    }

    public Currency getTo() {
        return to;
    }

    public double getRate() {
        return rate;
    }

    public double getAmount() {
        return amount;
    }

    public double getConvertedAmount() {
        return convertedAmount;
    }
}
