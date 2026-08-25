package com.example.vehicle_tracker.scheduler;

import com.example.vehicle_tracker.kafka.VehicleLocationProducer;
import com.example.vehicle_tracker.models.Location;
import com.example.vehicle_tracker.models.Vehicle;
import com.example.vehicle_tracker.repository.LocationRepository;
import com.example.vehicle_tracker.repository.VehicleRepository;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class LocationScheduler {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private VehicleLocationProducer vehicleLocationProducer;

    private final Faker faker = new Faker();

    private double[][] locations = {
            {40.7128, -74.0060},    // New York
            {51.5074, -0.1278},     // London
            {28.6139, 77.2090},     // Delhi
            {35.6762, 139.6503},    // Tokyo
            {-33.8688, 151.2093}    // Sydney
    };

    @Scheduled(fixedRate = 5000)
    public void generateLocations() {

        List<Vehicle> vehicles = vehicleRepository.findAll();

        for (int i = 0; i < vehicles.size(); i++) {

            Vehicle vehicle = vehicles.get(i);

            double latitude = locations[i][0];
            double longitude = locations[i][1];

            latitude += faker.number().randomDouble(4, -5,5) / 1000;
            longitude += faker.number().randomDouble(4, -5, 5) / 1000;

            locations[i][0] = latitude;
            locations[i][1] = longitude;

            int speed = (int) faker.number().randomDouble(2, 20, 100);

            Location location = new Location();

            location.setLatitude(latitude);
            location.setLongitude(longitude);
            location.setSpeed(speed);
            location.setVehicle(vehicle);
            location.setTimeStamp(LocalDateTime.now());

            vehicleLocationProducer.sendLocation(location);

            System.out.println(
                    vehicle.getVehicleNumber() +
                            " → " + latitude +
                            ", " + longitude
            );
        }
    }
}