package com.example.vehicle_tracker.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    private Vehicle vehicle;
    private double latitude;
    private double longitude;
    private int speed;
    private LocalDateTime timeStamp;
}
