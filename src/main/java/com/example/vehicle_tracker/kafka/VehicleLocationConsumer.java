package com.example.vehicle_tracker.kafka;

import com.example.vehicle_tracker.models.Location;
import com.example.vehicle_tracker.service.AlertService;
import com.example.vehicle_tracker.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class VehicleLocationConsumer {

    @Autowired
    private LocationService locationService;

    @Autowired
    private AlertService alertService;

    @KafkaListener(
            topics = "vehicle-location-topic",
            groupId = "vehicle-location-group"
    )
    public void consume(Location location){
        System.out.println("INFO: RECEIVING LOCATION DATA "+location);
        locationService.processLocation(location);
        alertService.checkSpeed(location);
    }
}
