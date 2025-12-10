package com.airline.reservation.service;

import com.airline.reservation.model.Flight;
import com.airline.reservation.model.Reservation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for FlightService class.
 */
class FlightServiceTest {
    private FlightService flightService;
    private Flight testFlight1;
    private Flight testFlight2;
    private Flight testFlight3;
    private LocalDateTime tomorrow;

    @BeforeEach
    void setUp() {
        flightService = new FlightService();
        tomorrow = LocalDateTime.now().plusDays(1).withHour(10).withMinute(0).withSecond(0).withNano(0);
        
        testFlight1 = new Flight("AA101", "New York", tomorrow, 50);
        testFlight2 = new Flight("AA102", "Los Angeles", tomorrow, 30);
        testFlight3 = new Flight("AA103", "New York", tomorrow.plusHours(5), 25);
        
        flightService.addFlight(testFlight1);
        flightService.addFlight(testFlight2);
        flightService.addFlight(testFlight3);
    }

    @Test
    @DisplayName("Search flights by destination and date returns matching flights")
    void testSearchFlights_ValidDestination_ReturnsMatchingFlights() {
        List<Flight> results = flightService.searchFlights("New York", tomorrow);
        
        assertEquals(2, results.size());
        assertTrue(results.stream().allMatch(f -> f.getDestination().equals("New York")));
    }

    @Test
    @DisplayName("Search flights with no matches returns empty list")
    void testSearchFlights_NoMatches_ReturnsEmptyList() {
        List<Flight> results = flightService.searchFlights("Miami", tomorrow);
        
        assertTrue(results.isEmpty());
    }

    @Test
    @DisplayName("Search flights with null destination throws exception")
    void testSearchFlights_NullDestination_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> 
            flightService.searchFlights(null, tomorrow)
        );
    }

    @Test
    @DisplayName("Search flights with null date throws exception")
    void testSearchFlights_NullDate_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> 
            flightService.searchFlights("New York", null)
        );
    }

    @Test
    @DisplayName("Search flights case insensitive on destination")
    void testSearchFlights_CaseInsensitive_ReturnsMatches() {
        List<Flight> results = flightService.searchFlights("new york", tomorrow);
        
        assertEquals(2, results.size());
    }

    @Test
    @DisplayName("Book flight successfully reduces available seats")
    void testBookFlight_Success_ReducesSeats() {
        int initialSeats = testFlight1.getAvailableSeats();
        
        Reservation reservation = flightService.bookFlight("John Doe", testFlight1, 5);
        
        assertNotNull(reservation);
        assertEquals("John Doe", reservation.getCustomerName());
        assertEquals(testFlight1, reservation.getFlight());
        assertEquals(5, reservation.getSeatsBooked());
        assertEquals(initialSeats - 5, testFlight1.getAvailableSeats());
    }

    @Test
    @DisplayName("Book flight with insufficient seats throws exception")
    void testBookFlight_InsufficientSeats_ThrowsException() {
        assertThrows(IllegalStateException.class, () -> 
            flightService.bookFlight("Jane Smith", testFlight1, 100)
        );
    }

    @Test
    @DisplayName("Book flight with zero seats throws exception")
    void testBookFlight_ZeroSeats_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> 
            flightService.bookFlight("Jane Smith", testFlight1, 0)
        );
    }

    @Test
    @DisplayName("Book flight with negative seats throws exception")
    void testBookFlight_NegativeSeats_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> 
            flightService.bookFlight("Jane Smith", testFlight1, -5)
        );
    }

    @Test
    @DisplayName("Book flight with null customer name throws exception")
    void testBookFlight_NullCustomerName_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> 
            flightService.bookFlight(null, testFlight1, 2)
        );
    }

    @Test
    @DisplayName("Book flight with null flight throws exception")
    void testBookFlight_NullFlight_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> 
            flightService.bookFlight("John Doe", null, 2)
        );
    }

    @Test
    @DisplayName("Book exact number of available seats succeeds")
    void testBookFlight_ExactSeats_Success() {
        Flight smallFlight = new Flight("AA999", "Boston", tomorrow, 5);
        flightService.addFlight(smallFlight);
        
        Reservation reservation = flightService.bookFlight("Alice Brown", smallFlight, 5);
        
        assertNotNull(reservation);
        assertEquals(0, smallFlight.getAvailableSeats());
    }

    @Test
    @DisplayName("Book one more than available seats throws exception")
    void testBookFlight_OneMoreThanAvailable_ThrowsException() {
        Flight smallFlight = new Flight("AA999", "Boston", tomorrow, 5);
        flightService.addFlight(smallFlight);
        
        assertThrows(IllegalStateException.class, () -> 
            flightService.bookFlight("Bob White", smallFlight, 6)
        );
    }

    @Test
    @DisplayName("Multiple bookings on same flight work correctly")
    void testBookFlight_MultipleBookings_ReduceSeatsCorrectly() {
        int initialSeats = testFlight1.getAvailableSeats();
        
        flightService.bookFlight("Customer1", testFlight1, 10);
        flightService.bookFlight("Customer2", testFlight1, 15);
        
        assertEquals(initialSeats - 25, testFlight1.getAvailableSeats());
        assertEquals(2, flightService.getAllReservations().size());
    }

    @Test
    @DisplayName("Get reservations by customer returns correct reservations")
    void testGetReservationsByCustomer_MultipleReservations_ReturnsAll() {
        flightService.bookFlight("John Doe", testFlight1, 5);
        flightService.bookFlight("John Doe", testFlight2, 3);
        flightService.bookFlight("Jane Smith", testFlight3, 2);
        
        List<Reservation> johnReservations = flightService.getReservationsByCustomer("John Doe");
        
        assertEquals(2, johnReservations.size());
        assertTrue(johnReservations.stream().allMatch(r -> r.getCustomerName().equals("John Doe")));
    }

    @Test
    @DisplayName("Get reservations for customer with no bookings returns empty list")
    void testGetReservationsByCustomer_NoReservations_ReturnsEmptyList() {
        List<Reservation> reservations = flightService.getReservationsByCustomer("Nonexistent Customer");
        
        assertTrue(reservations.isEmpty());
    }

    @Test
    @DisplayName("Get reservations is case insensitive")
    void testGetReservationsByCustomer_CaseInsensitive_ReturnsMatches() {
        flightService.bookFlight("John Doe", testFlight1, 5);
        
        List<Reservation> reservations = flightService.getReservationsByCustomer("john doe");
        
        assertEquals(1, reservations.size());
    }

    @Test
    @DisplayName("Search excludes flights with zero available seats")
    void testSearchFlights_ExcludesFullyBookedFlights() {
        Flight fullFlight = new Flight("AA777", "Miami", tomorrow, 2);
        flightService.addFlight(fullFlight);
        flightService.bookFlight("Customer", fullFlight, 2);
        
        List<Flight> results = flightService.searchFlights("Miami", tomorrow);
        
        assertTrue(results.isEmpty());
    }

    @Test
    @DisplayName("Add null flight throws exception")
    void testAddFlight_NullFlight_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> 
            flightService.addFlight(null)
        );
    }
}
