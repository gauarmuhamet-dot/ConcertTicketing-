
package com.concertticketing.strategy.pricing;

public class OldPricingCalculator {

    public double calculate(String type, double price,
                            int daysBefore, double demand) {

        PricingStrategy strategy;

        switch (type) {
            case "REGULAR":
                strategy = new RegularPricingStrategy();
                break;

            case "EARLY_BIRD":
                strategy = new EarlyBirdPricingStrategy();
                break;

            case "DEMAND":
                strategy = new DemandPricingStrategy(() -> demand);
                break;

            default:
                throw new IllegalArgumentException(
                        "Unknown pricing type");
        }

        PricingContext context = new PricingContext(strategy);

        return context.calculate(price, daysBefore, demand);
    }
}
