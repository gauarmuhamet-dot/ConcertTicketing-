
package com.concertticketing.strategy.pricing;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class OldPricingCalculatorTest {

    private final OldPricingCalculator calculator =
            new OldPricingCalculator();

    @Test
    void testRegularPricing() {
        assertEquals(10000, calculator.calculate(
                "REGULAR", 10000, 40, 0.5));
    }

    @Test
    void testEarlyBirdPricing() {
        assertEquals(8500, calculator.calculate(
                "EARLY_BIRD", 10000, 40, 0.5));
    }

    @Test
    void testDemandPricing() {
        assertEquals(15000, calculator.calculate(
                "DEMAND", 10000, 10, 0.9));
    }

    @Test
    void testLowDemandPricing() {
        assertEquals(9000, calculator.calculate(
                "DEMAND", 10000, 10, 0.2));
    }

    @Test
    void testUnknownPricing() {
        assertThrows(IllegalArgumentException.class,
                () -> calculator.calculate(
                        "UNKNOWN", 10000, 10, 0.5));
    }
}
