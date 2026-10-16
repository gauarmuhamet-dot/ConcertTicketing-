
package com.concertticketing.reservation;

import java.util.HashSet;
import java.util.Set;

public class SeatingChart {

    private final Set<String> availableSeats = new HashSet<>();

    public SeatingChart() {
        for (char row = 'A'; row <= 'C'; row++) {
            for (int number = 1; number <= 20; number++) {
                availableSeats.add(row + String.valueOf(number));
            }
        }
    }

    public boolean isAvailable(String seatNumber) {
        if (seatNumber == null) {
            return false;
        }
        return availableSeats.contains(seatNumber.trim().toUpperCase());
    }

    public void reserveSeat(String seatNumber) {
        if (!isAvailable(seatNumber)) {
            throw new IllegalStateException("Seat is not available");
        }
        availableSeats.remove(seatNumber.trim().toUpperCase());
    }

    public void releaseSeat(String seatNumber) {
        if (seatNumber == null || !seatNumber.trim().toUpperCase().matches("[ABC]([1-9]|1[0-9]|20)")) {
            throw new IllegalArgumentException("Invalid seat number");
        }

        availableSeats.add(seatNumber.trim().toUpperCase());
    }

    public int getAvailableSeatCount() {
        return availableSeats.size();
    }
}
