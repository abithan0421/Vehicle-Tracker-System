package com.example.vehicle_tracker.models;

import com.example.vehicle_tracker.enums.VehicleStatus;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String vehicleNumber;
    private String driverName;
    @Enumerated(EnumType.STRING)
    private VehicleStatus status;
}


