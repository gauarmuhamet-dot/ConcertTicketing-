package com.concertticketing.persistence;

public class ChainRecord {

    private final String reservationId;
    private ChainStatus status;

    public ChainRecord(String reservationId, ChainStatus status) {
        if (reservationId == null || reservationId.isBlank()) {
            throw new IllegalArgumentException("Reservation ID is required");
        }

        if (status == null) {
            throw new IllegalArgumentException("Chain status is required");
        }

        this.reservationId = reservationId;
        this.status = status;
    }

    public String getReservationId() {
        return reservationId;
    }

    public ChainStatus getStatus() {
        return status;
    }

    public void setStatus(ChainStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Chain status is required");
        }

        this.status = status;
    }
}