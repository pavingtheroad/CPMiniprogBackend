package com.changping.backend.repository;

import com.changping.backend.entity.repair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepairRepository extends JpaRepository<repair, Integer> {
    // 供提交报修的职工查询自己的申请
    List<repair> findByStaffId(String staffId);

    // 供维修人员查询 处理/未处理 的申请
    List<repair> findByHandle(Boolean handle);
}
