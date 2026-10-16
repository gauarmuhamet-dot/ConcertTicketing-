
package com.concertticketing.processing;

public class Ticket {

    public enum Status {
        ISSUED,
        USED,
        REFUNDED
    }

    private final String ticketId;
    private final String reservationId;
    private final String seatNumber;
    private final double price;
    private Status status;

    public Ticket(String ticketId, String reservationId,
                  String seatNumber, double price) {

        if (ticketId == null || ticketId.isBlank()) {
            throw new IllegalArgumentException("Ticket ID is required");
        }
        if (reservationId == null || reservationId.isBlank()) {
            throw new IllegalArgumentException("Reservation ID is required");
        }
        if (seatNumber == null || seatNumber.isBlank()) {
            throw new IllegalArgumentException("Seat number is required");
        }
        if (!Double.isFinite(price) || price <= 0) {
            throw new IllegalArgumentException("Invalid ticket price");
        }

        this.ticketId = ticketId;
        this.reservationId = reservationId;
        this.seatNumber = seatNumber;
        this.price = price;
        this.status = Status.ISSUED;
    }

    public void markUsed() {
        if (status != Status.ISSUED) {
            throw new IllegalStateException("Ticket cannot be used");
        }
        status = Status.USED;
    }

    public void refund() {
        if (status != Status.ISSUED) {
            throw new IllegalStateException("Ticket cannot be refunded");
        }
        status = Status.REFUNDED;
    }

    public String getTicketId() {
        return ticketId;
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
