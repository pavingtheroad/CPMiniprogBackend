package com.changping.backend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;

@Entity
@Data
public class repair {
    @Id
    private Integer id;
    private String name;
    @Column(name = "repair_type")
    private String repairType;
    @Column(name = "repair_location")
    private String repairLocation;
    @Column(name = "file_path")
    private String filePath;
    @Column(name = "repair_date")
    private Date repairDate;
    private String remarks;
    private Boolean handle;
    @Column(name = "staff_id")
    private String staffId;

    @ManyToOne
    @JoinColumn(name = "staff_id", referencedColumnName = "staff_id", insertable = false, updatable = false)
    @JsonBackReference // 避免无限递归
    private staff staff;
}
