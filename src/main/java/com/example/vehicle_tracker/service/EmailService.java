package com.example.vehicle_tracker.service;

import com.example.vehicle_tracker.models.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService implements AlertService{

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void checkSpeed(Location location) {
        if(location.getSpeed()>80)
            sendMail(location);
    }

    public void sendMail(Location location){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("abitharia@gmail.com");
        message.setSubject("Vehicle Speed Alert");
        message.setText(
                "Vehicle " + location.getVehicle().getVehicleNumber()
                        + " exceeded the speed limit. Speed: "
                        + location.getSpeed() + " km/h"
        );
        mailSender.send(message);
        System.out.println("INFO: MAIL HAS BEEN SENT");
    }
}
