package com.example.flight;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

/**
 * Simple console interface for users to search flights,
 * book a seat, and view reservations.
 */
public class FlightReservationApp {

    public static void main(String[] args) {

        FlightService flightService = new FlightService();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== Flight Reservation System ===");
            System.out.println("1. Search Flights");
            System.out.println("2. Book Flight");
            System.out.println("3. View My Reservations");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // flush newline

            switch (choice) {
                case 1 -> {
                    System.out.print("Enter destination: ");
                    String dest = scanner.nextLine();

                    System.out.print("Enter date (YYYY-MM-DD): ");
                    LocalDate date = LocalDate.parse(scanner.nextLine());

                    List<Flight> results = flightService.searchFlights(dest, date.atStartOfDay());

                    if (results.isEmpty()) {
                        System.out.println("No flights found.");
                    } else {
                        results.forEach(System.out::println);
                    }
                }
                case 2 -> {
                    System.out.print("Customer name: ");
                    String name = scanner.nextLine();

                    System.out.print("Enter flight number: ");
                    String flightNumber = scanner.nextLine();

                    // Find matching flight
                    Flight selected = flightService.searchFlights("", LocalDateTime.now())
                            .stream()
                            .filter(f -> f.getFlightNumber().equalsIgnoreCase(flightNumber))
                            .findFirst()
                            .orElse(null);

                    if (selected == null) {
                        System.out.println("Flight not found.");
                        break;
                    }

                    System.out.print("Seats to book: ");
                    int seats = scanner.nextInt();

                    try {
                        Reservation reservation = flightService.bookFlight(name, selected, seats);
                        System.out.println("Booking successful!");
                        System.out.println(reservation);
                    } catch (Exception ex) {
                        System.out.println("Booking failed: " + ex.getMessage());
                    }
                }
                case 3 -> {
                    System.out.print("Enter your name: ");
                    String name = scanner.nextLine();

                    List<Reservation> res = flightService.getReservationsFor(name);

                    if (res.isEmpty()) {
                        System.out.println("No reservations found.");
                    } else {
                        res.forEach(System.out::println);
                    }
                }
                case 4 -> {
                    System.out.println("Goodbye!");
                    System.exit(0);
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }
}
