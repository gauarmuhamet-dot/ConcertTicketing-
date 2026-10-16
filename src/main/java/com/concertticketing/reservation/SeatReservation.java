
package com.concertticketing.reservation;

public class SeatReservation {

    public enum Status {
        NEW,
        RESERVED,
        CANCELLED
    }

    private final String reservationId;
    private final String seatNumber;
    private final double price;
    private Status status;

    public SeatReservation(String reservationId, String seatNumber, double price) {
        if (reservationId == null || reservationId.isBlank()) {
            throw new IllegalArgumentException("Reservation ID is required");
        }
        if (seatNumber == null || seatNumber.isBlank()) {
            throw new IllegalArgumentException("Seat number is required");
        }
        if (!Double.isFinite(price) || price <= 0) {
            throw new IllegalArgumentException("Price must be positive");
        }

        this.reservationId = reservationId;
        this.seatNumber = seatNumber;
        this.price = price;
        this.status = Status.NEW;
    }

    public void reserve() {
        if (status != Status.NEW) {
            throw new IllegalStateException("Only NEW reservations can be reserved");
        }
        status = Status.RESERVED;
    }

    public void cancel() {
        if (status != Status.RESERVED) {
            throw new IllegalStateException("Only RESERVED reservations can be cancelled");
        }
        status = Status.CANCELLED;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public double getPrice() {
        return price;
    }

    public Status getStatus() {
        return status;
    }
}
