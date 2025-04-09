package com.changping.backend.service;

import com.changping.backend.entity.repair;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface RepairService {
    // 供提交报修的职工查询自己的申请
    List<repair> getRepairListByStaffId(String staffId);

    // 供维修人员查询 处理/未处理 的申请
    List<repair> getRepairListByHandled(Boolean handled);

    ResponseEntity<?> postRepairApply(repair repair);

    ResponseEntity<?> setHandled(Integer id, Boolean handled);
}
