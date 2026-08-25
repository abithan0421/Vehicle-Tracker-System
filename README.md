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
