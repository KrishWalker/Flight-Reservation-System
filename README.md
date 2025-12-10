# Flight Reservation System (Java)

A console-based flight reservation system built in Java that allows users to search for flights, book reservations, and manage their bookings.
Features

- Search Flights: Search for available flights by destination and date
- Book Flights: Reserve seats on available flights
- View Reservations: View all reservations for a specific customer
Real-time Seat Management: Prevents overbooking with synchronized booking operations

The focus of this exercise was to demonstrate clean object-oriented design, test coverage, and maintainable code.


---

## Features

- Search flights by destination and date
- Book seats with validation to prevent overbooking
- View reservations for a specific customer
- Thread-safe booking operations
- In-memory data storage for flights and reservations
- Unit tests using JUnit 5 

---

## Tech Stack Requirements

- Java 17  
- JUnit 5  
- Maven or Gradle (optional)  

---

## Running the Application

1 .Compile & run:
```bash
javac -d bin src/main/java/com/airline/reservation/**/*.java
java -cp bin com.airline.reservation.FlightReservationApp
```
Run Using IDE
- Open the project in IntelliJ IDEA, Eclipse, or VS Code
- Run FlightReservationApp.java
2. Running Tests
Using Maven
```bash
mvn test
```
Using Gradle
```bash
gradle test
```
Using IDE

Right-click FlightServiceTest.java and select Run Tests

---

## Usage Guide

Main Menu Options

Search for flights
- Enter destination city (e.g., "New York")
- Enter date in format: yyyy-MM-dd (e.g., "2024-12-15")
- View available flights with seat availability

Book a flight
- Enter your name
- Enter the flight number from search results
- Enter number of seats to book
- Receive confirmation with reservation ID

View my reservations
- Enter your name
- View all your current reservations

Exit
- Close the application 

---

## Sample Data

The application initializes some sample flights in code for quick testing:
- Multiple flights to New York, Los Angeles, Chicago, Miami, and Seattle
- Flights scheduled for tomorrow and the following days
- Different seat capacities per flight
This allows you to immediately search and book without manually seeding flights.

---

## Design Decisions

Separation of Concerns
- model package: domain entities
- service package: business logic
- Application class handles console interface only

Immutability and Validation
- Reservation objects are immutable
- Flight objects validate seat counts and changes

Thread Safety
- Booking method is synchronized to prevent concurrent overbooking

Error Handling
- Meaningful exceptions
- Input validation throughout

Scalability Considerations
The current version stores data in memory.
A production-ready design would use a database and include:
- Transaction management
- Connection pooling
- Locking for concurrent writes
- Query indexing

---

## Real-Life Considerations

1. Concurrency Control
Problem: Multiple users booking the same seats simultaneously
Solution: Synchronized booking method prevents race conditions

2. Data Integrity
Problem: Invalid data causing system failures
Solution: Comprehensive validation in constructors and methods

3. User Experience
Problem: Users entering data in different formats
Solution: Case-insensitive searches, clear error messages, formatted output

4. Edge Cases Handled
- Booking more seats than available
- Searching for non-existent destinations
- Invalid date formats
- Fully booked flights excluded from search results
- Zero or negative seat bookings rejected

5. Scalability Considerations
Current: In-memory storage
Production: Would use database (PostgreSQL/MySQL) with:
- Transaction management
- Connection pooling
- Optimistic/pessimistic locking
- Indexing on destination and date fields

---

## Testing Coverage

Test Categories
1. Happy Path Tests
- Successful flight search
- Successful booking
- Viewing reservations

2. Edge Case Tests
- Booking exact number of available seats
- Booking one more than available
- Zero available seats

3. Validation Tests
- Null parameters
- Invalid seat numbers
- Empty strings

4. Integration Tests
- Multiple bookings on same flight
- Multiple reservations per customer
- Case-insensitive operations

Test Results
All 24 unit tests pass with comprehensive coverage of:
- FlightService methods
- Edge cases
- Error conditions
- Business logic validation

---

## Future Enhancements

- Integration with a real database
- REST API layer
- Web UI instead of console
- Pricing rules & seat classes
- Payment integration and refunds.
- Seat selection (window/aisle).
- Multi-leg flights and connections.
- User authentication and loyalty programs.

---

## Contributing

- Fork the repository.
- Create a feature branch.
- Add or update tests for your changes.
- Run mvn test to ensure everything passes.
- Open a pull request with a clear description.

---
