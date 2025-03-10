package com.changping.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.sql.Date;

@Data
@Entity
@Table(name = "leave_request")
public class LeaveRequest {
    @Id
    private int id;
    private String name;
    @Column(name = "class")
    private String class_num;
    private String student_id;
    private String file_path;
    private String leave_type;
    private Date leave_date;
    private String remarks;
    private int staff_id;
}
