package com.changping.backend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String message;
    private String response;
    @Column(name = "staff_id")
    private String staffId;

    @ManyToOne
    @JoinColumn(name = "staff_id", referencedColumnName = "staff_id", insertable = false, updatable = false)
    @JsonBackReference // 避免无限递归
    private staff staff;
}
