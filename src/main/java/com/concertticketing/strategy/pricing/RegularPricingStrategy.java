
package com.concertticketing.strategy.pricing;

public class RegularPricingStrategy implements PricingStrategy {

    @Override
    public double calculate(double price, int daysBefore, double demand) {
        return price;
    }
}
