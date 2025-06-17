package com.changping.backend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class patrol {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int locationId;
    private String staffId;
    private LocalDateTime checkTime;

    @ManyToOne
    @JoinColumn(name = "staff_id", referencedColumnName = "staff_id", insertable = false, updatable = false)
    @JsonBackReference // 避免无限递归
    private staff staff;

    @ManyToOne
    @JoinColumn(name = "location_id", referencedColumnName = "location_id", insertable = false, updatable = false)
    private location location;
}
