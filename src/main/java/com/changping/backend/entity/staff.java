package com.changping.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
public class staff {
    @Id
    private int staff_id;
    private String name;
    private String password;
    private String permission;
}