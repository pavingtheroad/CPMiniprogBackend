package com.changping.backend.DTO;

import com.changping.backend.entity.location;
import com.changping.backend.entity.patrol;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PatrolDTO {
    private String checkLocation;
    private LocalDateTime checkTime;

    public PatrolDTO(String s, LocalDateTime checkTime) {
        this.checkLocation = s;
        this.checkTime = checkTime;
    }

    public static PatrolDTO fromPatrol(patrol patrol) {
        location location = patrol.getLocation();
        return new PatrolDTO(location != null ? location.getPlaces() : null, patrol.getCheckTime());
    }
}
