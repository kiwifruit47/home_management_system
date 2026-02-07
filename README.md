# Home Management System

## Overview

This project is a Java-based backend system for managing residential buildings
and apartments. It provides functionality for tracking buildings, apartments,
owners, occupants, employees, and companies, as well as generating and managing
monthly apartment taxes.

The system focuses on a clean separation of concerns using entity, DAO, DTO and service
layers.

### Key Features

- Company, employee, building, apartment, owner and occupant management
- Monthly apartment tax calculation and generation
- Tax payment status tracking (paid / unpaid)
- Aggregated tax statistics by apartment, building, employee, and company
- Scheduled monthly tax generation
- Export of monthly tax receipts to .txt files and appendage to .xls file
- Soft delete strategy
- Integration and utility tests

---

## Tech Stack

- **Java 21**
- **Hibernate / JPA**
- **Gradle**
- **JUnit 5**
- **Mockito**
- **H2**
- **Apache POI**

---

## Architecture Overview

The application follows a layered architecture:

- **Entity layer**
    - Defines database structure through JPA annotations
    - Provides a persistence model for the DAO layer
- **DAO layer**
    - Responsible for database access
    - Persists, reads, updates and soft deletes entities
- **Service layer**
    - Contains business logic
    - Coordinates DAOs
- **DTOs**
    - Used to transfer data between layers