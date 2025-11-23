package com.example.tour.service;

import com.example.tour.entity.Location;
import com.example.tour.repository.LocationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationService {

    private final LocationRepository locationRepository;

    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

//    public List<Location> getAllLocations() {
//        return locationRepository.findAll();
//    }

    public Location getLocationById(Long id) {
        return locationRepository.findById(id).orElse(null);
    }

    public List<Location> getAllLocations() {
        return locationRepository.findByStatus(Location.Status.ACTIVE);
    }

    public Location createLocation(Location loc) {
        loc.setStatus(Location.Status.ACTIVE);
        return locationRepository.save(loc);
    }

    public Location updateLocation(Long id, Location loc) {
        return locationRepository.findById(id).map(existing -> {
            existing.setName(loc.getName());
            existing.setCountry(loc.getCountry());
            existing.setProvince(loc.getProvince());
            existing.setDescription(loc.getDescription());
            return locationRepository.save(existing);
        }).orElseThrow(() -> new RuntimeException("Location not found"));
    }

    public void deleteLocation(Long id) {
        locationRepository.findById(id).ifPresent(loc -> {
            loc.setStatus(Location.Status.INACTIVE);
            locationRepository.save(loc);
        });
    }
}
