# Pet Care Scheduler

A comprehensive Java-based pet management system designed to help pet owners and veterinary clinics organize and track pet appointments, medical records, and health schedules efficiently.

## Project Overview

The Pet Care Scheduler is a command-line application that demonstrates core Java concepts including object-oriented programming, file I/O operations, date/time handling with time zones, and data collection management. This project was developed as a learning exercise to showcase proficiency in Java development.

## Features

### 1. **Pet Registration Management**
- Register new pets with unique IDs
- Store comprehensive pet information including:
  - Pet name and species
  - Age tracking
  - Owner details and contact information
  - Automatic registration date tracking
- Robust input validation to prevent duplicate entries and invalid data

### 2. **Appointment Scheduling**
- Schedule three types of appointments:
  - Vet Visits
  - Vaccinations
  - Grooming sessions
- Full date and time support with timezone awareness using `ZonedDateTime`
- Appointment notes for additional context
- Duplicate appointment detection to prevent scheduling conflicts
- Future-date validation to prevent past appointment entries

### 3. **Data Persistence**
- Save and load pet records from file storage
- Custom CSV-like format with delimiter escaping to prevent data corruption
- Automatic data loading on application startup
- Auto-save functionality when exiting the application

### 4. **Record Viewing & History Tracking**
- Display detailed pet profiles with all registered information
- View upcoming appointments sorted chronologically
- Access complete past appointment history
- Real-time separation of future and past appointments

### 5. **Health Reports & Analytics**
Generate multiple report types:
- **Upcoming Appointments Report**: View all appointments scheduled for the next 7 days
- **Overdue Vet Visits Report**: Identify pets that haven't had a vet visit in the last 6 months
- **Species Inventory Report**: Organize and view all pets grouped by species

## Technical Implementation

### Architecture & Design Patterns
- **Object-Oriented Design**: Four main classes with clear separation of concerns
  - `Pet`: Represents a pet entity with attributes and methods
  - `Appointment`: Encapsulates appointment data and formatting
  - `PetCareScheduler`: Main application logic and menu system
  - `IO`: Utility class for standardized input/output operations

### Key Technologies & Concepts
- **Collections Framework**: `HashMap` for efficient pet lookup, `ArrayList` for appointment storage
- **Java Streams API**: Functional programming for filtering and sorting appointments
- **Date/Time API**: `ZonedDateTime` for timezone-aware scheduling
- **File I/O**: `BufferedReader` and `BufferedWriter` for efficient file operations
- **Input Validation**: Comprehensive error handling for user inputs
- **String Formatting**: Custom formatting for readable output

### Data Structure
```
petMap: HashMap<String, Pet>
└── Each Pet contains:
    ├── petId (unique identifier)
    ├── name, species, age
    ├── ownerName, contactInfo
    ├── registrationDate
    └── appointments: List<Appointment>
        └── Each Appointment contains:
            ├── type (Vet Visit, Vaccination, Grooming)
            ├── dateTime (ZonedDateTime)
            └── notes
```

## Usage

### Running the Application
```bash
javac *.java
java PetCareScheduler
```

### Menu Options
1. **Register Pet** - Add a new pet to the system
2. **Schedule Appointment** - Create an appointment for an existing pet
3. **Store Data to File** - Manually save records (auto-saves on exit)
4. **Display Records** - View pet details and appointment history
5. **Generate Health Reports** - Create various analytical reports
6. **Exit** - Close the application (with automatic data saving)

### Example Workflow
```
1. Register a pet (e.g., "DOG001", "Max", "Golden Retriever", 5, "John Doe", "555-1234")
2. Schedule an appointment for vaccination (e.g., "2025-02-20T14:30:00+0530")
3. View upcoming appointments in the next 7 days
4. Generate a report for pets overdue for vet visits
5. Exit (data is automatically saved)
```

## Input Format Reference

### Date/Time Format
Use ISO-8601 format with timezone: `yyyy-MM-dd'T'HH:mm:ssZ`

Example: `2025-02-20T14:30:00+0530` (Feb 20, 2025 at 2:30 PM IST)

## File Structure

```
.
├── Appointment.java      - Appointment entity class
├── Pet.java             - Pet entity class
├── PetCareScheduler.java - Main application logic
├── IO.java              - Input/output utility class
├── pet_records.txt      - Data storage file (auto-generated)
└── README.md            - This file
```

## Code Quality Features

✅ **Input Validation**
- Non-empty string validation for all required fields
- Duplicate pet ID detection
- Numeric input validation with error handling
- Appointment type validation against allowed values
- Future-date verification for appointments

✅ **Data Integrity**
- Delimiter escaping to prevent corruption in CSV format
- Duplicate appointment detection
- Type capitalization standardization

✅ **Error Handling**
- Try-catch blocks for file I/O operations
- User-friendly error messages
- Exception propagation for critical errors

✅ **Code Organization**
- Modular method design with single responsibility
- Clear naming conventions
- Proper encapsulation with getters and setters
- Utility class for I/O operations

## Learning Outcomes

This project demonstrates proficiency in:
- Object-oriented programming principles (encapsulation, inheritance, polymorphism)
- Java Collections Framework (HashMap, ArrayList, List operations)
- Stream API for functional programming
- Date and time handling with timezone support
- File I/O operations with resource management
- Input validation and error handling
- Menu-driven application design
- Data persistence and serialization concepts

## Future Enhancement Ideas

- Database integration (SQL/NoSQL) for scalable data storage
- GUI implementation using JavaFX or Swing
- Email notification system for upcoming appointments
- Pet health history analytics and vaccination tracking
- Multi-user support with authentication
- Export reports to PDF format
- Integration with calendar APIs

## Requirements

- Java 11 or higher
- Standard Java libraries (java.util, java.time, java.io)

## Notes for Repository

This project is intended as a demonstration of Java learning and is suitable for educational purposes. All code follows standard Java conventions and best practices.

---

**Created as a learning project to demonstrate Java proficiency**
