
package com.concertticketing.strategy.seats;

import java.util.List;

public class ClosestSeatStrategy implements SeatSelectionStrategy {

    @Override
    public String selectSeat(List<String> availableSeats) {
        if (availableSeats == null || availableSeats.isEmpty()) {
            return null;
        }

        return availableSeats.stream()
                .min(String::compareTo)
                .orElse(null);
    }
}
