
package com.concertticketing.strategy.pricing;

public class PricingContext {

    private PricingStrategy strategy;

    public PricingContext(PricingStrategy strategy) {
        setStrategy(strategy);
    }

    public void setStrategy(PricingStrategy strategy) {
        if (strategy == null) {
            throw new IllegalArgumentException("Strategy cannot be null");
        }
        this.strategy = strategy;
    }

    public double calculate(double price, int daysBefore, double demand) {
        return strategy.calculate(price, daysBefore, demand);
    }
}
