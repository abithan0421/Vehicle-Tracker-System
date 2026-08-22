package com.example.vehicle_tracker.cotroller;

import com.example.vehicle_tracker.models.Vehicle;
import com.example.vehicle_tracker.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class VehicleController {

    @Autowired
    VehicleService vehicleService;

    @GetMapping("vehicles")
    public ResponseEntity<List<Vehicle>> getAllVehicles(){
        return new ResponseEntity<>(vehicleService.getAllVehicles(), HttpStatus.OK);
    }

    @GetMapping("vehicle/{id}")
    public ResponseEntity<Vehicle> getVehicleById(@PathVariable("id") long id){
        return new ResponseEntity<>(vehicleService.getVehicleById(id), HttpStatus.OK);
    }

}
