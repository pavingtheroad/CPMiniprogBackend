package com.changping.backend.repository;


import com.changping.backend.entity.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeaveRepository extends JpaRepository<LeaveRequest, Integer> {
    List<LeaveRequest> findByNameOrStudentId(String name,String student_id);

    // JpaRepository 提供了 save(LeaveRequest leaveRequest) 方法

    List<LeaveRequest> findByStaffId(String staff_id);

    List<LeaveRequest> findAll();
}
