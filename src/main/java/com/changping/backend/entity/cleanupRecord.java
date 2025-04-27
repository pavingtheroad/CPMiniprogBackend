package com.changping.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class cleanupRecord {
    @Id
    private long id;

    @Column(name = "last_cleanup_time")
    private LocalDateTime lastCleanupTime;
}
