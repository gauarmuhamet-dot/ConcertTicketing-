
package com.concertticketing.strategy.pricing;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class PricingStrategyTest {

    @Test
    void testRegularPricing() {
        PricingStrategy strategy = new RegularPricingStrategy();
        assertEquals(10000, strategy.calculate(10000, 40, 0.5));
    }

    @Test
    void testEarlyBirdPricing() {
        PricingStrategy strategy = new EarlyBirdPricingStrategy();
        assertEquals(8500, strategy.calculate(10000, 40, 0.5));
        assertEquals(10000, strategy.calculate(10000, 10, 0.5));
    }

    @Test
    void testDemandPricing() {
        DemandDataSource fakeSource = () -> 0.9;
        PricingStrategy strategy = new DemandPricingStrategy(fakeSource);

        assertEquals(15000, strategy.calculate(10000, 10, 0.5));
    }

    @Test
    void testLowDemandPricing() {
        DemandDataSource fakeSource = () -> 0.2;
        PricingStrategy strategy = new DemandPricingStrategy(fakeSource);

        assertEquals(9000, strategy.calculate(10000, 10, 0.5));
    }

    @Test
    void testPricingSelector() {
        PricingSelector selector = new PricingSelector();

        assertInstanceOf(DemandPricingStrategy.class,
                selector.select(10, 0.9));

        assertInstanceOf(EarlyBirdPricingStrategy.class,
                selector.select(40, 0.5));

        assertInstanceOf(RegularPricingStrategy.class,
                selector.select(10, 0.5));
    }

    @Test
    void testRuntimeSwitching() {
        PricingContext context =
                new PricingContext(new RegularPricingStrategy());

        assertEquals(10000, context.calculate(10000, 40, 0.5));

        context.setStrategy(new EarlyBirdPricingStrategy());

        assertEquals(8500, context.calculate(10000, 40, 0.5));
    }

    static Stream<PricingStrategy> strategies() {
        return Stream.of(
                new RegularPricingStrategy(),
                new EarlyBirdPricingStrategy(),
                new DemandPricingStrategy(() -> 0.9)
        );
    }

    @ParameterizedTest
    @MethodSource("strategies")
    void priceMustNotBeNegative(PricingStrategy strategy) {
        assertTrue(strategy.calculate(10000, 40, 0.5) >= 0);
    }
}
