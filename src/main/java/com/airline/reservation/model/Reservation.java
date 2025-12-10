package com.airline.reservation.model;

import java.util.UUID;

/**
 * Represents a customer's flight reservation.
 * Immutable once created.
 */
public class Reservation {
    private final String reservationId;
    private final String customerName;
    private final Flight flight;
    private final int seatsBooked;

    public Reservation(String customerName, Flight flight, int seatsBooked) {
        if (customerName == null || customerName.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer name cannot be null or empty");
        }
        if (flight == null) {
            throw new IllegalArgumentException("Flight cannot be null");
        }
        if (seatsBooked <= 0) {
            throw new IllegalArgumentException("Must book at least one seat");
        }
        
        this.reservationId = UUID.randomUUID().toString();
        this.customerName = customerName;
        this.flight = flight;
        this.seatsBooked = seatsBooked;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public Flight getFlight() {
        return flight;
    }

    public int getSeatsBooked() {
        return seatsBooked;
    }

    @Override
    public String toString() {
        return String.format("Reservation ID: %s%nCustomer: %s%nFlight: %s%nSeats Booked: %d",
                reservationId, customerName, flight.getFlightNumber(), seatsBooked);
    }
}
