package com.airline.reservation.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a flight in the airline system.
 * Immutable except for availableSeats which can be modified during booking.
 */
public class Flight {
    private final String flightNumber;
    private final String destination;
    private final LocalDateTime departureTime;
    private int availableSeats;

    public Flight(String flightNumber, String destination, LocalDateTime departureTime, int availableSeats) {
        if (flightNumber == null || flightNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Flight number cannot be null or empty");
        }
        if (destination == null || destination.trim().isEmpty()) {
            throw new IllegalArgumentException("Destination cannot be null or empty");
        }
        if (departureTime == null) {
            throw new IllegalArgumentException("Departure time cannot be null");
        }
        if (availableSeats < 0) {
            throw new IllegalArgumentException("Available seats cannot be negative");
        }
        
        this.flightNumber = flightNumber;
        this.destination = destination;
        this.departureTime = departureTime;
        this.availableSeats = availableSeats;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public String getDestination() {
        return destination;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    /**
     * Reduces available seats by the specified amount.
     * This method is package-private to be accessed only by FlightService.
     */
    public void reduceSeats(int seats) {
        if (seats < 0) {
            throw new IllegalArgumentException("Cannot reduce by negative seats");
        }
        if (availableSeats - seats < 0) {
            throw new IllegalStateException("Not enough seats available");
        }
        this.availableSeats -= seats;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Flight flight = (Flight) o;
        return Objects.equals(flightNumber, flight.flightNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(flightNumber);
    }

    @Override
    public String toString() {
        return String.format("Flight %s to %s departing at %s (%d seats available)",
                flightNumber, destination, departureTime, availableSeats);
    }
}
