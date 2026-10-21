
package com.concertticketing.strategy.pricing;

public class PricingSelector {

    public PricingStrategy select(int daysBefore, double demand) {

        if (demand > 0.8) {
            return new DemandPricingStrategy();
        }

        if (daysBefore >= 30) {
            return new EarlyBirdPricingStrategy();
        }

        return new RegularPricingStrategy();
    }
}
