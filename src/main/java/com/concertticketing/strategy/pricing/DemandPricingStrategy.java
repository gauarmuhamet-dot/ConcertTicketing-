
package com.concertticketing.strategy.pricing;

public class DemandPricingStrategy implements PricingStrategy {

    private final DemandDataSource dataSource;

    public DemandPricingStrategy() {
        this(new SimulatedDemandDataSource());
    }

    public DemandPricingStrategy(DemandDataSource dataSource) {
        if (dataSource == null) {
            throw new IllegalArgumentException("Data source cannot be null");
        }
        this.dataSource = dataSource;
    }

    @Override
    public double calculate(double price, int daysBefore, double demand) {

        double currentDemand = dataSource.getDemand();

        if (currentDemand > 0.8) {
            return price * 1.5;
        }

        if (currentDemand < 0.3) {
            return price * 0.9;
        }

        return price;
    }
}
