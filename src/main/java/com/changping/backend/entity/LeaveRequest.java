package com.changping.backend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
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
    @Column(name = "student_id")
    private String studentId;
    private String file_path;
    private String leave_type;
    private Date leave_date;
    private String remarks;
    @Column(name = "staff_id")
    private String staffId;

    @ManyToOne
    @JoinColumn(name = "staff_id", referencedColumnName = "staff_id", insertable = false, updatable = false)
    @JsonBackReference // 避免无限递归
    private staff staff;
}
