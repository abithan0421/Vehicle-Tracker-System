package com.example.vehicle_tracker.cotroller;

import com.example.vehicle_tracker.models.Location;
import com.example.vehicle_tracker.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LocationController {

    @Autowired
    LocationService locationService;

    @GetMapping("vehicle/{id}/locations")
    public ResponseEntity<List<Location>> getAllLocationsByVehicle(@PathVariable("id") long id){
        return new ResponseEntity<>(locationService.getAllLocationsByVehicle(id), HttpStatus.OK);
    }

    @GetMapping("vehicle/{id}/location")
    public ResponseEntity<Location> getLocationByVehicle(@PathVariable("id") long id){
        return new ResponseEntity<>(locationService.getLocationByVehicle(id), HttpStatus.OK);
    }
}
