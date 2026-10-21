
package com.concertticketing.strategy.seats;

import java.util.List;

public class CheapestSeatStrategy implements SeatSelectionStrategy {

    @Override
    public String selectSeat(List<String> availableSeats) {
        if (availableSeats == null || availableSeats.isEmpty()) {
            return null;
        }

        return availableSeats.stream()
                .max(String::compareTo)
                .orElse(null);
    }
}
