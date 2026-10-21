
package com.concertticketing.strategy.pricing;

public class EarlyBirdPricingStrategy implements PricingStrategy {

    @Override
    public double calculate(double price, int daysBefore, double demand) {
        if (daysBefore >= 30) {
            return price * 0.85;
        }
        return price;
    }
}
