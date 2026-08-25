# Real-Time Vehicle Tracker System

A backend-focused vehicle tracking system built with **Java and Spring Boot**, demonstrating REST API development, JPA/Hibernate persistence, PostgreSQL, Apache Kafka event streaming, scheduled vehicle-location generation, Grafana monitoring, and event-driven email alerts.

The project simulates vehicle movement, publishes location events through Kafka, processes those events using a Kafka consumer, stores location data in PostgreSQL, visualizes tracking data through Grafana, and sends email alerts when a vehicle exceeds the configured speed limit.

---

## Overview

The system simulates multiple vehicles moving through predefined geographical locations.

Every few seconds, the application generates updated vehicle-location data and publishes it to Apache Kafka.

The Kafka consumer receives these events and processes them independently:

- Location data is persisted to PostgreSQL using **Spring Data JPA / Hibernate**
- Vehicle speed is checked for alert conditions
- Email notifications are triggered when the configured speed limit is exceeded
- Grafana reads the stored data from PostgreSQL and displays real-time tracking information

This project demonstrates a simple **event-driven architecture** without introducing unnecessary microservice complexity.

---

## Architecture

```text
                    ┌──────────────────────┐
                    │  Location Scheduler  │
                    │  Fake Vehicle Data   │
                    └──────────┬───────────┘
                               │
                               ▼
                    ┌──────────────────────┐
                    │   Kafka Producer     │
                    │   KafkaTemplate      │
                    └──────────┬───────────┘
                               │
                               ▼
                  ┌─────────────────────────┐
                  │ Kafka Topic              │
                  │ vehicle-location         │
                  └────────────┬────────────┘
                               │
                               ▼
                    ┌──────────────────────┐
                    │   Kafka Consumer     │
                    │   @KafkaListener     │
                    └───────┬────────┬─────┘
                            │        │
                    LocationService  AlertService
                            │        │
                            │        ▼
                            │   EmailService
                            │        │
                            │        ▼
                            │     Email
                            │
                            ▼
                   ┌─────────────────────┐
                   │     PostgreSQL      │
                   │    JPA / Hibernate  │
                   └──────────┬──────────┘
                              │
                              ▼
                     ┌─────────────────┐
                     │     Grafana     │
                     │    Dashboard    │
                     └─────────────────┘


## Technology Stack
| Technology      | Purpose                      |
| --------------- | ---------------------------- |
| Java            | Backend development          |
| Spring Boot     | Application framework        |
| Spring Web      | REST APIs                    |
| Spring Data JPA | Data access                  |
| Hibernate       | ORM                          |
| PostgreSQL      | Relational database          |
| Apache Kafka    | Event streaming              |
| Spring Kafka    | Kafka integration            |
| Grafana         | Monitoring and visualization |
| Maven           | Dependency management        |
| Git             | Version control              |


## Project Structure
src/main/java/com/example/vehicle_tracker
│
├── controller
│   └── VehicleController.java
│
├── models
│   ├── Vehicle.java
│   └── Location.java
│
├── repository
│   ├── VehicleRepository.java
│   └── LocationRepository.java
│
├── service
│   ├── LocationService.java
│   ├── AlertService.java
│   └── EmailService.java
│
├── kafka
│   ├── KafkaConfig.java
│   ├── VehicleLocationProducer.java
│   └── VehicleLocationConsumer.java
│
└── scheduler
    └── LocationScheduler.java

## Database — PostgreSQL

The application uses **PostgreSQL** as the persistent database and **Spring Data JPA + Hibernate** for ORM and database access.

### Database Setup

- Install PostgreSQL and make sure the PostgreSQL server is running.
- Create the application database:

```sql
CREATE DATABASE VehicleTrackerDb;

```application.properties
spring.datasource.url=jdbc:postgresql://localhost:5432/VehicleTrackerDb
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.datasource.driver-class-name=org.postgresql.Driver

spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

### Database Architecture
Vehicle
------------------------------------------------
id
vehicleNumber
driverName
status
                    │
                    │ 1 : Many
                    ▼
Location
------------------------------------------------
id
latitude
longitude
speed
timeStamp
vehicle

### Apache Kafka

Apache Kafka is used as the event streaming layer of the application.

Instead of directly passing vehicle-location data from the scheduler to the business logic, the application publishes each location as an event to a Kafka topic.

```text
Location Scheduler
        │
        ▼
Kafka Producer
        │
        ▼
vehicle-location
        │
        ▼
Kafka Consumer
        │
        ▼
Business Processing

# Start Kafka

Before starting the Kafka server, configure the Kafka JVM heap size in Command Prompt:

```cmd
set KAFKA_HEAP_OPTS=-Xmx1G -Xms1G

### Grafana Dashboard

The dashboard provides a real-time view of the vehicle tracking information.

It includes:
 -> Current speed of each vehicle
 -> Vehicle location
 -> Speed history
 -> Location history
