package com.example.flight;

import java.time.LocalDateTime;

/**
 * Represents a flight in the airline system.
 * This class holds basic metadata like flight number,
 * destination, departure time, and available seats.
 */
public class Flight {

    private String flightNumber;
    private String destination;
    private LocalDateTime departureTime;
    private int availableSeats;

    public Flight(String flightNumber, String destination, LocalDateTime departureTime, int availableSeats) {
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

    public void reduceAvailableSeats(int seats) {
        this.availableSeats -= seats;
    }

    @Override
    public String toString() {
        return flightNumber + " | " + destination + " | " + departureTime + " | Seats: " + availableSeats;
    }
}
