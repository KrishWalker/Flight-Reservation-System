package com.airline.reservation.service;

import com.airline.reservation.model.Flight;
import com.airline.reservation.model.Reservation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class that manages flight searches and reservations.
 * Thread-safe for concurrent booking operations.
 */
public class FlightService {
    private final List<Flight> flights;
    private final List<Reservation> reservations;

    public FlightService() {
        this.flights = new ArrayList<>();
        this.reservations = new ArrayList<>();
    }

    /**
     * Adds a flight to the system.
     */
    public void addFlight(Flight flight) {
        if (flight == null) {
            throw new IllegalArgumentException("Flight cannot be null");
        }
        flights.add(flight);
    }

    /**
     * Searches for flights to a specific destination on a given date.
     * 
     * @param destination The destination city
     * @param date The date to search for flights
     * @return List of available flights matching the criteria
     */
    public List<Flight> searchFlights(String destination, LocalDateTime date) {
        if (destination == null || destination.trim().isEmpty()) {
            throw new IllegalArgumentException("Destination cannot be null or empty");
        }
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }

        LocalDate searchDate = date.toLocalDate();
        
        return flights.stream()
                .filter(flight -> flight.getDestination().equalsIgnoreCase(destination))
                .filter(flight -> flight.getDepartureTime().toLocalDate().equals(searchDate))
                .filter(flight -> flight.getAvailableSeats() > 0)
                .collect(Collectors.toList());
    }

    /**
     * Books a flight for a customer.
     * This method is synchronized to prevent race conditions when multiple bookings occur.
     * 
     * @param customerName The name of the customer
     * @param flight The flight to book
     * @param seats Number of seats to book
     * @return The created Reservation object
     * @throws IllegalStateException if not enough seats are available
     */
    public synchronized Reservation bookFlight(String customerName, Flight flight, int seats) {
        if (customerName == null || customerName.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer name cannot be null or empty");
        }
        if (flight == null) {
            throw new IllegalArgumentException("Flight cannot be null");
        }
        if (seats <= 0) {
            throw new IllegalArgumentException("Must book at least one seat");
        }
        if (!flights.contains(flight)) {
            throw new IllegalArgumentException("Flight not found in the system");
        }
        if (flight.getAvailableSeats() < seats) {
            throw new IllegalStateException(
                    String.format("Not enough seats available. Requested: %d, Available: %d", 
                            seats, flight.getAvailableSeats()));
        }

        // Reduce available seats
        flight.reduceSeats(seats);

        // Create and store reservation
        Reservation reservation = new Reservation(customerName, flight, seats);
        reservations.add(reservation);

        return reservation;
    }

    /**
     * Retrieves all reservations for a specific customer.
     * 
     * @param customerName The customer's name
     * @return List of reservations for the customer
     */
    public List<Reservation> getReservationsByCustomer(String customerName) {
        if (customerName == null || customerName.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer name cannot be null or empty");
        }

        return reservations.stream()
                .filter(reservation -> reservation.getCustomerName().equalsIgnoreCase(customerName))
                .collect(Collectors.toList());
    }

    /**
     * Gets all flights in the system (primarily for testing purposes).
     */
    public List<Flight> getAllFlights() {
        return new ArrayList<>(flights);
    }

    /**
     * Gets all reservations in the system (primarily for testing purposes).
     */
    public List<Reservation> getAllReservations() {
        return new ArrayList<>(reservations);
    }
}
