
package com.concertticketing.strategy.pricing;

public interface PricingStrategy {

    double calculate(double price, int daysBefore, double demand);

}
