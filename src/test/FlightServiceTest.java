package com.example.flight;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

public class FlightServiceTest {

    @Test
    void testSearchFlights_NoResults() {
        FlightService service = new FlightService();

        List<Flight> flights = service.searchFlights("Tokyo",
                LocalDateTime.now().plusDays(5));

        Assertions.assertTrue(flights.isEmpty());
    }

    @Test
    void testBookFlight_Success() {
        FlightService service = new FlightService();

        Flight flight = service.searchFlights("New York",
                LocalDateTime.now().plusDays(1))
                .get(0);

        int before = flight.getAvailableSeats();

        Reservation reservation = service.bookFlight("John Doe", flight, 2);

        Assertions.assertEquals(2, reservation.getSeatsBooked());
        Assertions.assertEquals(before - 2, flight.getAvailableSeats());
    }

    @Test
    void testBookFlight_NotEnoughSeats() {
        FlightService service = new FlightService();

        Flight flight = service.searchFlights("London",
                LocalDateTime.now().plusDays(1)).get(0);

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                service.bookFlight("John Doe", flight, 999));
    }
}
