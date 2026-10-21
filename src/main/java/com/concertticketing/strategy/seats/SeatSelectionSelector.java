
package com.concertticketing.strategy.seats;

public class SeatSelectionSelector {

    public SeatSelectionStrategy select(String preference) {

        if ("CHEAP".equalsIgnoreCase(preference)) {
            return new CheapestSeatStrategy();
        }

        if ("CLOSE".equalsIgnoreCase(preference)) {
            return new ClosestSeatStrategy();
        }

        return new BalancedSeatStrategy();
    }
}
