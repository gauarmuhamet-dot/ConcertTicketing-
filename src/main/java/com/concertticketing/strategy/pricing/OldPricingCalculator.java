
package com.concertticketing.strategy.pricing;

public class OldPricingCalculator {

    public double calculate(String type, double price,
                            int daysBefore, double demand) {
        if (type.equals("REGULAR")) {
            return price;
        } else if (type.equals("EARLY_BIRD")) {
            if (daysBefore >= 30) {
                return price * 0.85;
            }
            return price;
        } else if (type.equals("DEMAND")) {
            if (demand > 0.8) {
                return price * 1.5;
            } else if (demand < 0.3) {
                return price * 0.9;
            }
            return price;
        }
        throw new IllegalArgumentException("Unknown pricing type");
    }
}
