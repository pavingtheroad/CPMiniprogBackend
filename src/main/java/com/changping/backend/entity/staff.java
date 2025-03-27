package com.changping.backend.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class staff {
    @Id
    @Column(name = "staff_id")
    private String staffId;
    private String name;
    private String password;
    private String permission;

    @OneToMany(mappedBy = "staff", cascade = CascadeType.ALL)
    @JsonManagedReference // 避免无限递归
    private List<LeaveRequest> leaveRequests;
}