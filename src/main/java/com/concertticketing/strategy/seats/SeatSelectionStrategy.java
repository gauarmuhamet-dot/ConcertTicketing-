
package com.concertticketing.strategy.seats;

import java.util.List;

public interface SeatSelectionStrategy {

    String selectSeat(List<String> availableSeats);

}
