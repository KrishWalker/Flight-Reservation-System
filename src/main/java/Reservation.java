package com.example.flight;

/**
 * Represents a customer reservation.
 * A reservation simply binds a customer to a specific
 * flight along with the number of seats booked.
 */
public class Reservation {

    private String customerName;
    private Flight flight;
    private int seatsBooked;

    public Reservation(String customerName, Flight flight, int seatsBooked) {
        this.customerName = customerName;
        this.flight = flight;
        this.seatsBooked = seatsBooked;
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
        return "Reservation: " + customerName + " | Flight: " + flight.getFlightNumber() +
                " | Seats Booked: " + seatsBooked;
    }
}
