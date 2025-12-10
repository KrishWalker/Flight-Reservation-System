package com.airline.reservation;

import com.airline.reservation.model.Flight;
import com.airline.reservation.model.Reservation;
import com.airline.reservation.service.FlightService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

/**
 * Console-based flight reservation application.
 */
public class FlightReservationApp {
    private final FlightService flightService;
    private final Scanner scanner;
    private final DateTimeFormatter dateFormatter;

    public FlightReservationApp() {
        this.flightService = new FlightService();
        this.scanner = new Scanner(System.in);
        this.dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        initializeSampleData();
    }

    /**
     * Initializes sample flight data for demonstration.
     */
    private void initializeSampleData() {
        LocalDateTime tomorrow = LocalDate.now().plusDays(1).atTime(10, 0);
        
        flightService.addFlight(new Flight("AA101", "New York", tomorrow, 50));
        flightService.addFlight(new Flight("AA102", "Los Angeles", tomorrow, 30));
        flightService.addFlight(new Flight("AA103", "Chicago", tomorrow.plusHours(2), 40));
        flightService.addFlight(new Flight("AA104", "New York", tomorrow.plusHours(5), 25));
        flightService.addFlight(new Flight("AA105", "Miami", tomorrow.plusDays(1), 60));
        flightService.addFlight(new Flight("AA106", "Seattle", tomorrow.plusDays(1), 35));
    }

    public void start() {
        System.out.println("===========================================");
        System.out.println("  Welcome to the Flight Reservation System");
        System.out.println("===========================================\n");

        boolean running = true;
        while (running) {
            displayMenu();
            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    searchFlights();
                    break;
                case 2:
                    bookFlight();
                    break;
                case 3:
                    viewReservations();
                    break;
                case 4:
                    running = false;
                    System.out.println("\nThank you for using our service. Goodbye!");
                    break;
                default:
                    System.out.println("\nInvalid choice. Please try again.\n");
            }
        }
        scanner.close();
    }

    private void displayMenu() {
        System.out.println("Main Menu:");
        System.out.println("1. Search for flights");
        System.out.println("2. Book a flight");
        System.out.println("3. View my reservations");
        System.out.println("4. Exit");
        System.out.println();
    }

    private void searchFlights() {
        System.out.println("\n--- Search Flights ---");
        
        System.out.print("Enter destination: ");
        String destination = scanner.nextLine().trim();

        System.out.print("Enter date (yyyy-MM-dd): ");
        String dateStr = scanner.nextLine().trim();

        try {
            LocalDate date = LocalDate.parse(dateStr, dateFormatter);
            LocalDateTime dateTime = date.atStartOfDay();

            List<Flight> flights = flightService.searchFlights(destination, dateTime);

            if (flights.isEmpty()) {
                System.out.println("\nNo flights found for " + destination + " on " + dateStr);
            } else {
                System.out.println("\nAvailable Flights:");
                System.out.println("--------------------------------------------------");
                for (Flight flight : flights) {
                    System.out.printf("Flight: %s | Destination: %s | Departure: %s | Available Seats: %d%n",
                            flight.getFlightNumber(),
                            flight.getDestination(),
                            flight.getDepartureTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                            flight.getAvailableSeats());
                }
                System.out.println("--------------------------------------------------");
            }
        } catch (DateTimeParseException e) {
            System.out.println("\nInvalid date format. Please use yyyy-MM-dd (e.g., 2024-12-25)");
        } catch (Exception e) {
            System.out.println("\nError searching flights: " + e.getMessage());
        }
        System.out.println();
    }

    private void bookFlight() {
        System.out.println("\n--- Book a Flight ---");

        System.out.print("Enter your name: ");
        String customerName = scanner.nextLine().trim();

        System.out.print("Enter flight number: ");
        String flightNumber = scanner.nextLine().trim();

        int seats = getIntInput("Enter number of seats: ");

        try {
            // Find the flight
            Flight flight = flightService.getAllFlights().stream()
                    .filter(f -> f.getFlightNumber().equalsIgnoreCase(flightNumber))
                    .findFirst()
                    .orElse(null);

            if (flight == null) {
                System.out.println("\nFlight not found. Please check the flight number.");
            } else {
                Reservation reservation = flightService.bookFlight(customerName, flight, seats);
                System.out.println("\n✓ Booking successful!");
                System.out.println("--------------------------------------------------");
                System.out.println(reservation);
                System.out.println("--------------------------------------------------");
            }
        } catch (IllegalStateException e) {
            System.out.println("\n✗ Booking failed: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("\n✗ Error during booking: " + e.getMessage());
        }
        System.out.println();
    }

    private void viewReservations() {
        System.out.println("\n--- View Reservations ---");

        System.out.print("Enter your name: ");
        String customerName = scanner.nextLine().trim();

        try {
            List<Reservation> reservations = flightService.getReservationsByCustomer(customerName);

            if (reservations.isEmpty()) {
                System.out.println("\nNo reservations found for " + customerName);
            } else {
                System.out.println("\nYour Reservations:");
                System.out.println("==================================================");
                for (Reservation reservation : reservations) {
                    System.out.println(reservation);
                    System.out.println("Destination: " + reservation.getFlight().getDestination());
                    System.out.println("Departure: " + reservation.getFlight().getDepartureTime()
                            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
                    System.out.println("--------------------------------------------------");
                }
            }
        } catch (Exception e) {
            System.out.println("\nError retrieving reservations: " + e.getMessage());
        }
        System.out.println();
    }

    private int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    public static void main(String[] args) {
        FlightReservationApp app = new FlightReservationApp();
        app.start();
    }
}
