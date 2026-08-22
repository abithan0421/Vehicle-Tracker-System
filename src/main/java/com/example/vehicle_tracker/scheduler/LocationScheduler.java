package com.example.vehicle_tracker.scheduler;

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
    VehicleRepository vehicleRepository;
    @Autowired
    LocationRepository locationRepository;

    private final Faker faker = new Faker();

    @Scheduled(fixedRate = 5000)
    public void generateLocations(){
        List<Vehicle> vehicles = vehicleRepository.findAll();
        for(Vehicle vehicle:vehicles){
            double latitude = faker.number().randomDouble(6,8,13);
            double longitude = faker.number().randomDouble(6,70,80);
            int speed = (int)faker.number().randomDouble(2,20,100);
            Location location = new Location();
            location.setLatitude(latitude);
            location.setLongitude(longitude);
            location.setSpeed(speed);
            location.setVehicle(vehicle);
            location.setTimeStamp(LocalDateTime.now());
            locationRepository.save(location);
            System.out.println("Generating locations: "+vehicle.getVehicleNumber());
        }
    }
}
