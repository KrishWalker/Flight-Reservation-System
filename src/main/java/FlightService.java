package com.example.flight;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service layer that manages flights and reservations.
 * Uses in-memory storage for this exercise.
 */
public class FlightService {

    private List<Flight> flights = new ArrayList<>();
    private List<Reservation> reservations = new ArrayList<>();

    public FlightService() {
        seedFlights(); // Pre-load sample flights
    }

    /**
     * Searches flights based on destination and date.
     * Only returns flights leaving on the same calendar date.
     */
    public List<Flight> searchFlights(String destination, LocalDateTime date) {

        return flights.stream()
                .filter(f -> f.getDestination().equalsIgnoreCase(destination))
                .filter(f -> f.getDepartureTime().toLocalDate().isEqual(date.toLocalDate()))
                .collect(Collectors.toList());
    }

    /**
     * Attempts to book the requested seats.
     * If not enough seats are available, the booking does NOT proceed.
     */
    public Reservation bookFlight(String customerName, Flight flight, int seats) {

        if (flight.getAvailableSeats() < seats) {
            throw new IllegalArgumentException("Not enough available seats.");
        }

        flight.reduceAvailableSeats(seats);

        Reservation reservation = new Reservation(customerName, flight, seats);
        reservations.add(reservation);
        return reservation;
    }

    public List<Reservation> getReservationsFor(String customerName) {
        return reservations.stream()
                .filter(r -> r.getCustomerName().equalsIgnoreCase(customerName))
                .collect(Collectors.toList());
    }

    private void seedFlights() {
        flights.add(new Flight("AI101", "New York",
                LocalDateTime.now().plusDays(1).withHour(10).withMinute(0), 50));
        flights.add(new Flight("AI202", "London",
                LocalDateTime.now().plusDays(1).withHour(14).withMinute(30), 30));
        flights.add(new Flight("AI303", "New York",
                LocalDateTime.now().plusDays(2).withHour(9).withMinute(15), 20));
    }
}
