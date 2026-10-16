
package com.concertticketing.reservation;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SeatReservationTest {

    @Test
    void testNewReservation() {
        SeatReservation reservation =
                new SeatReservation("R001", "A5", 15000.0);

        assertEquals("R001", reservation.getReservationId());
        assertEquals("A5", reservation.getSeatNumber());
        assertEquals(15000.0, reservation.getPrice());
    }

    @Test
    void testReserve() {
        SeatReservation reservation =
                new SeatReservation("R002", "B5", 12000.0);

        reservation.reserve();

        assertEquals(SeatReservation.Status.RESERVED,
                reservation.getStatus());
    }

    @Test
    void testCancelReservation() {
        SeatReservation reservation =
                new SeatReservation("R003", "C5", 10000.0);

        reservation.reserve();
        reservation.cancel();

        assertEquals(SeatReservation.Status.CANCELLED,
                reservation.getStatus());
    }

    @Test
    void testCancelWithoutReservation() {
        SeatReservation reservation =
                new SeatReservation("R004", "A6", 15000.0);

        assertThrows(IllegalStateException.class,
                reservation::cancel);
    }

    @Test
    void testDoubleReserve() {
        SeatReservation reservation =
                new SeatReservation("R005", "B6", 12000.0);

        reservation.reserve();

        assertThrows(IllegalStateException.class,
                reservation::reserve);
    }
}
