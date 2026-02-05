package ru.rostislav.model;

public class ExchangeRate {
    private int id;
    private int baseCurrencyId;
    private int targetCurrencyId;
    private double rate;

    public ExchangeRate(int id, int baseCurrencyId, int targetCurrencyId, double rate) {
        this.id = id;
        this.baseCurrencyId = baseCurrencyId;
        this.targetCurrencyId = targetCurrencyId;
        this.rate = rate;
    }

    public int getId() {
        return id;
    }

    public int getBaseCurrencyId() {
        return baseCurrencyId;
    }

    public int getTargetCurrencyId() {
        return targetCurrencyId;
    }

    public double getRate() {
        return rate;
    }
}
