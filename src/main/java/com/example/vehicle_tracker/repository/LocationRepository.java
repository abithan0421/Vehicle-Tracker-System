package com.example.vehicle_tracker.repository;

import com.example.vehicle_tracker.models.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    List<Location> findByVehicleIdOrderByTimeStampDesc(Long vehicleId);
    Location findTopByVehicleIdOrderByTimeStampDesc(Long vehicleId);
}
