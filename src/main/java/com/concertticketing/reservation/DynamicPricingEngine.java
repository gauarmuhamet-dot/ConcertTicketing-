
package com.concertticketing.reservation;

public class DynamicPricingEngine {

    private static final double BASE_PRICE = 10000.0;

    public double calculatePrice(String seatNumber) {
        if (seatNumber == null || seatNumber.isBlank()) {
            throw new IllegalArgumentException("Seat number is required");
        }

        String seat = seatNumber.trim().toUpperCase();

        if (seat.matches("A[1-9][0-9]*")) {
            return BASE_PRICE * 1.5;
        } else if (seat.matches("B[1-9][0-9]*")) {
            return BASE_PRICE * 1.2;
        } else if (seat.matches("C[1-9][0-9]*")) {
            return BASE_PRICE;
        } else {
            throw new IllegalArgumentException("Invalid seat number");
        }
    }
}
