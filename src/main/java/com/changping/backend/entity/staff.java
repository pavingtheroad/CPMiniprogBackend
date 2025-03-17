package com.changping.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;

@Data
@Entity
@Builder
public class staff {
    @Id
    private int staff_id;
    private String name;
    private String password;
    private String permission;
}
