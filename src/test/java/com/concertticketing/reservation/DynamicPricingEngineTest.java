
package com.concertticketing.reservation;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DynamicPricingEngineTest {

    @Test
    void testSectionA() {
        DynamicPricingEngine engine = new DynamicPricingEngine();

        assertEquals(15000.0, engine.calculatePrice("A5"));
    }

    @Test
    void testSectionB() {
        DynamicPricingEngine engine = new DynamicPricingEngine();

        assertEquals(12000.0, engine.calculatePrice("B5"));
    }

    @Test
    void testSectionC() {
        DynamicPricingEngine engine = new DynamicPricingEngine();

        assertEquals(10000.0, engine.calculatePrice("C5"));
    }

    @Test
    void testInvalidSeat() {
        DynamicPricingEngine engine = new DynamicPricingEngine();

        assertThrows(IllegalArgumentException.class,
                () -> engine.calculatePrice("Z5"));
    }

    @Test
    void testNullSeat() {
        DynamicPricingEngine engine = new DynamicPricingEngine();

        assertThrows(IllegalArgumentException.class,
                () -> engine.calculatePrice(null));
    }
}
