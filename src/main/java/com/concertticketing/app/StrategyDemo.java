
package com.concertticketing.app;

import com.concertticketing.strategy.pricing.*;
import com.concertticketing.strategy.seats.*;

import java.util.List;

public class StrategyDemo {

    public static void main(String[] args) {

        System.out.println("=== ASSIGNMENT 5: STRATEGY PATTERN ===");

        double price = 10000;
        int daysBefore = 40;
        double demand = 0.9;

        System.out.println("\n1. ALL PRICING STRATEGIES");

        PricingStrategy[] strategies = {
                new RegularPricingStrategy(),
                new EarlyBirdPricingStrategy(),
                new DemandPricingStrategy()
        };

        for (PricingStrategy strategy : strategies) {
            PricingContext context = new PricingContext(strategy);

            System.out.println(
                    strategy.getClass().getSimpleName() + ": " +
                            context.calculate(price, daysBefore, demand)
            );
        }

        System.out.println("\n2. MANUAL STRATEGY SWITCHING");

        PricingContext pricing =
                new PricingContext(new RegularPricingStrategy());

        System.out.println("Before: " +
                pricing.calculate(price, daysBefore, demand));

        pricing.setStrategy(new EarlyBirdPricingStrategy());

        System.out.println("After: " +
                pricing.calculate(price, daysBefore, demand));

        System.out.println("\n3. AUTOMATIC PRICING SELECTOR");

        PricingSelector pricingSelector = new PricingSelector();

        pricing.setStrategy(pricingSelector.select(daysBefore, demand));

        System.out.println("Selected: " +
                pricing.calculate(price, daysBefore, demand));

        System.out.println("\n4. ALL SEAT STRATEGIES");

        List<String> seats = List.of("A1", "B2", "C3", "D4", "E5");

        SeatSelectionStrategy[] seatStrategies = {
                new CheapestSeatStrategy(),
                new ClosestSeatStrategy(),
                new BalancedSeatStrategy()
        };

        for (SeatSelectionStrategy strategy : seatStrategies) {
            SeatSelectionContext context =
                    new SeatSelectionContext(strategy);

            System.out.println(
                    strategy.getClass().getSimpleName() + ": " +
                            context.selectSeat(seats)
            );
        }

        System.out.println("\n5. AUTOMATIC SEAT SELECTOR");

        SeatSelectionSelector seatSelector =
                new SeatSelectionSelector();

        SeatSelectionContext seatContext =
                new SeatSelectionContext(
                        seatSelector.select("CLOSE")
                );

        System.out.println("Selected seat: " +
                seatContext.selectSeat(seats));

        System.out.println("\n6. RUNTIME SEAT SWITCHING");

        seatContext.setStrategy(new BalancedSeatStrategy());

        System.out.println("After switching: " +
                seatContext.selectSeat(seats));

        System.out.println("\n=== DEMO COMPLETED ===");
    }
}
