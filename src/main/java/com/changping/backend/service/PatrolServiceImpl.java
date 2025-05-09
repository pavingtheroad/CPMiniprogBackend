package com.changping.backend.service;

import com.changping.backend.entity.patrol;
import com.changping.backend.entity.location;
import com.changping.backend.repository.LocationRepository;
import com.changping.backend.repository.PatrolRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class PatrolServiceImpl implements PatrolService{
    private final PatrolRepository patrolRepository;
    private final LocationRepository locationRepository;
    public PatrolServiceImpl(PatrolRepository patrolRepository, LocationRepository locationRepository) {
        this.patrolRepository = patrolRepository;
        this.locationRepository = locationRepository;
    }

    @Override
    public List<patrol> getTodayPatrolsByStaffId(String staffId) {
        LocalDate today = LocalDate.now();
        LocalDateTime start = today.atStartOfDay(); // 00:00:00
        LocalDateTime end = today.atTime(LocalTime.MAX); // 23:59:59.999999999
        return patrolRepository.findByStaffIdAndCheckTimeBetween(staffId, start, end);
    }

    @Override
    public ResponseEntity<?> checkPatrol(String location, String staffId) {
        try{
            location found = locationRepository.findByPlaces(location);
            if (found != null) {
                LocalDateTime now = LocalDateTime.now();
                patrol findPatrol = patrolRepository.findByStaffIdAndLocation(staffId, location);
                if (findPatrol != null) {
                    findPatrol.setCheckTime(now);
                }
                else {
                    patrol newPatrol = new patrol();
                    newPatrol.setLocation(location);
                    newPatrol.setStaffId(staffId);
                    newPatrol.setCheckTime(now);
                    patrolRepository.save(newPatrol);
                }
                return ResponseEntity.ok().body("Patrol is updated");
            } else {
                return ResponseEntity.notFound().build();
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to update checkPatrol: " + e.getMessage());
        }

    }
}
