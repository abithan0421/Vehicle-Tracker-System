# Real-Time Vehicle Tracker System

A backend-focused **real-time vehicle tracking system** built with **Java and Spring Boot**. The project demonstrates REST API development, Spring Data JPA, Hibernate, PostgreSQL persistence, Apache Kafka event streaming, scheduled data generation, Grafana visualization, and event-driven email alerts.

The system simulates multiple vehicles moving through predefined geographical locations. Vehicle location events are continuously generated, published to Kafka, consumed asynchronously, stored in PostgreSQL, visualized through Grafana, and evaluated for speed-limit violations.

---

## Overview

The application simulates real-time vehicle movement using scheduled location generation.

Every few seconds, the system generates a new location for each vehicle containing information such as:

* Vehicle number
* Latitude
* Longitude
* Current speed
* Timestamp

The generated location is published as an event to an Apache Kafka topic.

A Kafka consumer receives the event and performs the following operations:

1. Processes the vehicle-location event.
2. Stores the location in PostgreSQL.
3. Checks whether the vehicle exceeds the configured speed limit.
4. Triggers an email notification when a speed violation occurs.
5. Makes the stored data available for visualization through Grafana.

The project demonstrates an **event-driven backend architecture** while keeping the implementation simple enough to understand and run as a single Spring Boot application.

---

## Architecture

```text
                    ┌─────────────────────────┐
                    │   Location Scheduler    │
                    │  Generates Vehicle Data │
                    └────────────┬────────────┘
                                 │
                                 ▼
                    ┌─────────────────────────┐
                    │    Kafka Producer       │
                    │     KafkaTemplate       │
                    └────────────┬────────────┘
                                 │
                                 ▼
              ┌──────────────────────────────────┐
              │          Apache Kafka             │
              │                                  │
              │      Topic: vehicle-location     │
              └────────────────┬─────────────────┘
                               │
                               ▼
                    ┌─────────────────────────┐
                    │    Kafka Consumer       │
                    │     @KafkaListener      │
                    └────────────┬────────────┘
                                 │
                    ┌────────────┴────────────┐
                    │                         │
                    ▼                         ▼
          ┌──────────────────┐      ┌──────────────────┐
          │ Location Service │      │  Alert Service   │
          └────────┬─────────┘      └────────┬─────────┘
                   │                         │
                   ▼                         ▼
          ┌──────────────────┐      ┌──────────────────┐
          │    PostgreSQL    │      │  Email Service   │
          │  JPA / Hibernate │      └────────┬─────────┘
          └────────┬─────────┘               │
                   │                         ▼
                   │                    Email Alert
                   │
                   ▼
          ┌──────────────────┐
          │      Grafana     │
          │    Dashboard     │
          └──────────────────┘
```

### Event Flow

```text
Vehicle Location Generated
          ↓
Kafka Producer
          ↓
vehicle-location Kafka Topic
          ↓
Kafka Consumer
          ↓
     ┌────┴─────┐
     ↓          ↓
PostgreSQL   Speed Check
     ↓          ↓
  Grafana    Email Alert
```

The scheduler does not directly call the database or alert functionality. Instead, vehicle-location information is published as an event to Kafka. This provides loose coupling between event generation and event processing.

---

## Technology Stack

| Technology           | Purpose                              |
| -------------------- | ------------------------------------ |
| **Java**             | Backend development                  |
| **Spring Boot**      | Application framework                |
| **Spring Web**       | REST API development                 |
| **Spring Data JPA**  | Database access                      |
| **Hibernate**        | ORM / persistence                    |
| **PostgreSQL**       | Relational database                  |
| **Apache Kafka**     | Event streaming                      |
| **Spring Kafka**     | Kafka integration                    |
| **Grafana**          | Data visualization and monitoring    |
| **Spring Scheduler** | Periodic vehicle-location generation |
| **Spring Mail**      | Email notifications                  |
| **Maven**            | Dependency management                |
| **Git**              | Version control                      |

---

## Project Structure

```text
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
```

### Component Responsibilities

**VehicleController**

Provides REST endpoints for interacting with vehicle-related data.

**LocationScheduler**

Generates simulated vehicle locations at a fixed interval.

**VehicleLocationProducer**

Publishes vehicle-location events to the Kafka topic.

**VehicleLocationConsumer**

Consumes vehicle-location events from Kafka and delegates them to the appropriate services.

**LocationService**

Processes and persists vehicle-location information.

**AlertService**

Checks vehicle speed against the configured speed limit and determines whether an alert should be triggered.

**EmailService**

Sends email notifications for speed-limit violations.

**Repositories**

Provide database access through Spring Data JPA.

---

# 🗄️ Database — PostgreSQL

The application uses **PostgreSQL** for persistent storage and **Spring Data JPA + Hibernate** for object-relational mapping.

## Database Setup

### 1. Install PostgreSQL

Make sure PostgreSQL is installed and the PostgreSQL server is running.

### 2. Create the Database

Open PostgreSQL/psql and execute:

```sql
CREATE DATABASE VehicleTrackerDb;
```

### 3. Configure the Application

Update `application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/VehicleTrackerDb
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.datasource.driver-class-name=org.postgresql.Driver

spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

Replace `your_username` and `your_password` with your PostgreSQL credentials.

---

## 🧩 Database Architecture

The application maintains a relationship between vehicles and their historical locations.

```text
Vehicle
------------------------------------------------
id
vehicleNumber
driverName
status
------------------------------------------------
                 │
                 │ 1 : Many
                 ▼
Location
------------------------------------------------
id
latitude
longitude
speed
timestamp
vehicle
------------------------------------------------
```

One vehicle can have multiple location records.

This allows the application to maintain vehicle-location history and enables Grafana to visualize changes in speed and location over time.

---

# Apache Kafka

Apache Kafka acts as the **event streaming layer** of the application.

Instead of directly passing location information from the scheduler to the business services, the application publishes each location update as an event to Kafka.

## Kafka Event Flow

```text
Location Scheduler
        │
        ▼
Kafka Producer
        │
        ▼
vehicle-location
     Topic
        │
        ▼
Kafka Consumer
        │
        ▼
Business Processing
   ┌────┴─────┐
   ▼          ▼
Database   Speed Check
              │
              ▼
         Email Alert
```

### Kafka Topic

```text
vehicle-location
```

This topic contains the vehicle-location events generated by the application.

---

# Running Apache Kafka

Before starting Kafka, configure the Kafka JVM heap size.

Open **Command Prompt** and execute:

```cmd
set KAFKA_HEAP_OPTS=-Xmx1G -Xms1G
```

Then start the Kafka server:

```cmd
kafka-server-start.bat C:\kafka\config\server.properties
```

> Update the Kafka installation path if Kafka is installed in a different directory.

Make sure Kafka is running before starting the Spring Boot application.

---

# Vehicle Location Simulation

The application uses a scheduled task to simulate vehicle movement.

The scheduler periodically generates new location information for registered vehicles.

Example:

```text
Vehicle: TN01AB1234

Latitude:  8.5241
Longitude: 76.9366
Speed:     62 km/h
Timestamp: 2026-08-25 14:30:10
```

The generated information is converted into a vehicle-location event and published to Kafka.

This allows the project to simulate a real-time tracking environment without requiring physical GPS devices.

---

# Speed-Limit Alerts

The system contains an event-driven speed-monitoring mechanism.

When the Kafka consumer receives a vehicle-location event, the vehicle's current speed is checked against the configured speed limit.

Example:

```text
Configured Speed Limit: 80 km/h

Vehicle Speed: 65 km/h
Result: Normal
```

```text
Configured Speed Limit: 80 km/h

Vehicle Speed: 95 km/h
Result: Speed Violation
```

When a violation occurs:

```text
Vehicle Location Event
        ↓
Kafka Consumer
        ↓
Speed Check
        ↓
Speed > Limit
        ↓
Alert Service
        ↓
Email Service
        ↓
Email Notification
```

This demonstrates how Kafka events can trigger business actions asynchronously.

---

# Email Notifications

When a vehicle exceeds the configured speed limit, the application sends an email notification.

Example notification:

```text
Subject:
Vehicle Speed Alert

Message:
Vehicle TN01AB1234 has exceeded the configured speed limit.

Current Speed: 95 km/h
Speed Limit: 80 km/h
Location:
Latitude: 8.5241
Longitude: 76.9366
```

The email functionality is separated into its own service so that alert logic and email delivery remain independently manageable.
<img width="1568" height="649" alt="image" src="https://github.com/user-attachments/assets/b30550f0-4c9d-4a5e-979d-e0e340594b70" />

---

# Grafana Dashboard

Grafana is used to visualize vehicle tracking data stored in PostgreSQL.

The dashboard provides a real-time view of vehicle activity and historical tracking information.

### Dashboard Metrics

The dashboard can display:

*  Current vehicle speed
*  Current vehicle location
*  Speed history
*  Location history
*  Vehicle activity over time
*  Speed-limit violations

### Data Flow

```text
Vehicle Event
     ↓
Kafka
     ↓
Kafka Consumer
     ↓
PostgreSQL
     ↓
Grafana
     ↓
Vehicle Dashboard
```

Grafana connects directly to PostgreSQL and queries the stored location data for visualization.
<img width="1920" height="882" alt="image" src="https://github.com/user-attachments/assets/4eca437e-28bf-49b8-96a9-48f30f2fcfc0" />

---

#  REST API

The application also exposes REST APIs for vehicle-related operations.

Example endpoint:

```http
GET /vehicles
```

Example response:

```json
[
  {
    "id": 1,
    "vehicleNumber": "TN01AB1234",
    "driverName": "John",
    "status": "ACTIVE"
  }
]
```

The REST API provides a simple interface for retrieving vehicle information independently of the Kafka event-processing pipeline.

---

#  Complete End-to-End Flow

The complete system works as follows:

```text
1. Scheduler generates vehicle location
                 ↓
2. Kafka Producer publishes event
                 ↓
3. Kafka stores event in vehicle-location topic
                 ↓
4. Kafka Consumer receives event
                 ↓
5. Location is persisted to PostgreSQL
                 ↓
6. Speed is checked against configured limit
                 ↓
       ┌─────────┴─────────┐
       ↓                   ↓
   Normal Speed       Speed Violation
                           ↓
                     Email Alert
                           
PostgreSQL
     ↓
Grafana
     ↓
Real-Time Dashboard
```

---

#  Configuration

The main application configuration is maintained in:

```text
src/main/resources/application.properties
```

Example:

```properties
spring.application.name=vehicle-tracker

spring.datasource.url=jdbc:postgresql://localhost:5432/VehicleTrackerDb
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.datasource.driver-class-name=org.postgresql.Driver

spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

spring.kafka.bootstrap-servers=localhost:9092
```

Keep sensitive credentials such as database passwords and email credentials out of source control.

---

#  How to Run the Project

## 1. Clone the Repository

```bash
git clone <your-repository-url>
cd vehicle-tracker
```

## 2. Start PostgreSQL

Make sure PostgreSQL is running and the `VehicleTrackerDb` database has been created.

## 3. Start Kafka

Configure the Kafka heap:

```cmd
set KAFKA_HEAP_OPTS=-Xmx1G -Xms1G
```

Start Kafka:

```cmd
kafka-server-start.bat C:\kafka\config\server.properties
```

## 4. Start the Spring Boot Application

Using Maven:

```bash
mvn spring-boot:run
```

Or run the main Spring Boot class from your IDE.

## 5. Verify the Application

Once the application starts, the scheduler should begin generating vehicle-location events.

The expected flow is:

```text
Scheduler
   ↓
Kafka Producer
   ↓
Kafka Topic
   ↓
Kafka Consumer
   ↓
PostgreSQL
   ↓
Grafana
```

If a vehicle exceeds the configured speed limit:

```text
Speed Violation
      ↓
Alert Service
      ↓
Email Notification
```

---

# 🧪 Example Scenario

Assume the system has three vehicles:

```text
TN01AB1234
TN02CD5678
TN03EF9012
```

The scheduler generates their locations every few seconds.

Example events:

```text
TN01AB1234 → 55 km/h
TN02CD5678 → 72 km/h
TN03EF9012 → 95 km/h
```

If the configured speed limit is `80 km/h`:

```text
TN01AB1234 → Normal
TN02CD5678 → Normal
TN03EF9012 → Speed Violation
```

The location information for all three vehicles is stored in PostgreSQL, while the third vehicle additionally triggers an email alert.

Grafana can then display the vehicle activity and speed history.

---

#  Key Concepts Demonstrated

This project demonstrates practical implementation of:

* REST API development with Spring Boot
* Dependency Injection
* Spring Data JPA
* Hibernate ORM
* PostgreSQL database integration
* Entity relationships
* Apache Kafka producers and consumers
* Event-driven architecture
* Kafka topic-based communication
* Scheduled background processing
* Asynchronous business processing
* Email notifications
* Real-time data visualization
* Grafana dashboards
* Maven dependency management
* Git version control

---

# 🔮 Future Improvements

Possible future enhancements include:

* Real GPS device integration
* WebSocket-based live vehicle tracking
* Interactive map visualization
* Authentication and authorization using Spring Security
* Role-based access control
* Redis caching
* Docker and Docker Compose deployment
* Kafka consumer retry and dead-letter topics
* Multiple Kafka partitions for higher throughput
* Vehicle geofencing
* Fuel-consumption tracking
* Driver behavior analysis
* Historical route visualization
* Prometheus-based application metrics
* Kubernetes deployment
* Microservice-based architecture

---

#  Why This Project?

The project was designed to demonstrate how a backend application can process continuously generated data using an **event-driven architecture**.

Rather than building unnecessary microservices, the application keeps the core business logic within a single Spring Boot application while using Kafka where asynchronous event processing provides real architectural value.

The project therefore demonstrates practical backend concepts including:

```text
REST APIs
   +
Database Persistence
   +
Event Streaming
   +
Scheduled Processing
   +
Business Rules
   +
Email Notifications
   +
Data Visualization
```

---

##  License

This project is intended for educational and portfolio purposes.
