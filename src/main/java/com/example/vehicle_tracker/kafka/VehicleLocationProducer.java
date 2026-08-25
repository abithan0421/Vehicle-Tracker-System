package com.example.vehicle_tracker.kafka;

import com.example.vehicle_tracker.models.Location;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class VehicleLocationProducer {

    @Autowired
    private KafkaTemplate<String, Location> kafkaTemplate;

    public void sendLocation(Location location){
        System.out.println("INFO: SENDING LOCATION DATA");
        kafkaTemplate.send(
                "vehicle-location-topic",
                String.valueOf(location.getVehicle().getVehicleNumber()),
                location);
    }
}
