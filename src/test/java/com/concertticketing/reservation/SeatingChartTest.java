
package com.concertticketing.reservation;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SeatingChartTest {

    @Test
    void testSeatAvailability() {
        SeatingChart chart = new SeatingChart();

        assertTrue(chart.isAvailable("A5"));
    }

    @Test
    void testReserveSeat() {
        SeatingChart chart = new SeatingChart();

        chart.reserveSeat("A5");

        assertFalse(chart.isAvailable("A5"));
    }

    @Test
    void testReleaseSeat() {
        SeatingChart chart = new SeatingChart();

        chart.reserveSeat("B5");
        chart.releaseSeat("B5");

        assertTrue(chart.isAvailable("B5"));
    }

    @Test
    void testDoubleBooking() {
        SeatingChart chart = new SeatingChart();

        chart.reserveSeat("A5");

        assertThrows(IllegalStateException.class,
                () -> chart.reserveSeat("A5"));
    }
}
