
package com.concertticketing.strategy.seats;

import java.util.List;

public class SeatSelectionContext {

    private SeatSelectionStrategy strategy;

    public SeatSelectionContext(SeatSelectionStrategy strategy) {
        setStrategy(strategy);
    }

    public void setStrategy(SeatSelectionStrategy strategy) {
        if (strategy == null) {
            throw new IllegalArgumentException("Strategy cannot be null");
        }
        this.strategy = strategy;
    }

    public String selectSeat(List<String> availableSeats) {
        return strategy.selectSeat(availableSeats);
    }
}
