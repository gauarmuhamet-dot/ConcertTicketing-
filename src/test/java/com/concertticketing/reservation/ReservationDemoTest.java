
package com.concertticketing.reservation;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class ReservationDemoTest {

    @Test
    void testReservationDemo() {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        try {
            System.setOut(new PrintStream(
                    output, true, StandardCharsets.UTF_8));

            ReservationDemo.main(new String[0]);

        } finally {
            System.setOut(originalOut);
        }

        String result = output.toString(StandardCharsets.UTF_8);

        assertTrue(result.contains("Reservation ID: R001"));
        assertTrue(result.contains("Seat: A5"));
        assertTrue(result.contains("Price:"));
        assertTrue(result.contains("Status: RESERVED"));

        ReservationRepository repository =
                new ReservationRepository(
                        Path.of("data", "reservations.csv"));

        SeatReservation saved = repository.findById("R001");

        assertNotNull(saved);
        assertEquals("A5", saved.getSeatNumber());
        assertEquals(SeatReservation.Status.RESERVED,
                saved.getStatus());
    }
}
