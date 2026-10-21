
package com.concertticketing.strategy.pricing;

public class SimulatedDemandDataSource implements DemandDataSource {

    @Override
    public double getDemand() {
        return 0.9;
    }
}
