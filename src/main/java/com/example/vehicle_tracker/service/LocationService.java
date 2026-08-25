package com.example.vehicle_tracker.service;

import com.example.vehicle_tracker.models.Location;
import com.example.vehicle_tracker.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationService {
    @Autowired
    LocationRepository locationRepository;

    public List<Location> getAllLocationsByVehicle(long id) {
        return locationRepository.findByVehicleIdOrderByTimeStampDesc(id);
    }

    public Location getLocationByVehicle(long id) {
        return locationRepository.findTopByVehicleIdOrderByTimeStampDesc(id);
    }

    public void processLocation(Location location) {
        System.out.println("INCOMING UPS DATA:");
        locationRepository.save(location);
    }
}
