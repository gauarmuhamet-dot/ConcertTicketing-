
package com.concertticketing.strategy.seats;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BalancedSeatStrategy implements SeatSelectionStrategy {

    @Override
    public String selectSeat(List<String> availableSeats) {
        if (availableSeats == null || availableSeats.isEmpty()) {
            return null;
        }

        List<String> sorted = new ArrayList<>(availableSeats);
        Collections.sort(sorted);

        return sorted.get(sorted.size() / 2);
    }
}
