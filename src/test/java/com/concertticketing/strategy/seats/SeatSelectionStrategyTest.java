
package com.concertticketing.strategy.seats;

import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SeatSelectionStrategyTest {

    private final List<String> seats =
            List.of("A1", "B2", "C3", "D4", "E5");

    @Test
    void testCheapestSeat() {
        SeatSelectionStrategy strategy = new CheapestSeatStrategy();
        assertEquals("E5", strategy.selectSeat(seats));
    }

    @Test
    void testClosestSeat() {
        SeatSelectionStrategy strategy = new ClosestSeatStrategy();
        assertEquals("A1", strategy.selectSeat(seats));
    }

    @Test
    void testBalancedSeat() {
        SeatSelectionStrategy strategy = new BalancedSeatStrategy();
        assertEquals("C3", strategy.selectSeat(seats));
    }

    @Test
    void testSelectorCheap() {
        SeatSelectionSelector selector = new SeatSelectionSelector();
        assertInstanceOf(CheapestSeatStrategy.class,
                selector.select("CHEAP"));
    }

    @Test
    void testSelectorClose() {
        SeatSelectionSelector selector = new SeatSelectionSelector();
        assertInstanceOf(ClosestSeatStrategy.class,
                selector.select("CLOSE"));
    }

    @Test
    void testSelectorBalanced() {
        SeatSelectionSelector selector = new SeatSelectionSelector();
        assertInstanceOf(BalancedSeatStrategy.class,
                selector.select("BALANCED"));
    }

    @Test
    void testRuntimeSwitching() {
        SeatSelectionContext context =
                new SeatSelectionContext(new ClosestSeatStrategy());

        assertEquals("A1", context.selectSeat(seats));

        context.setStrategy(new BalancedSeatStrategy());

        assertEquals("C3", context.selectSeat(seats));
    }

    @Test
    void testEmptySeats() {
        assertNull(new CheapestSeatStrategy().selectSeat(List.of()));
        assertNull(new ClosestSeatStrategy().selectSeat(List.of()));
        assertNull(new BalancedSeatStrategy().selectSeat(List.of()));
    }
}
