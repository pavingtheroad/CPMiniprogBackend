package com.changping.backend.service;

import com.changping.backend.DTO.StaffDTO;
import com.changping.backend.entity.staff;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AdministrationService {
    Page<StaffDTO> getStaffsInPage(int page, int size);

    /**
     * 根据姓名获得用户信息，排除重名问题
     * @param name
     * @return
     */
    List<StaffDTO> getStaffsByName(String name);

    /**
     * 管理员修改用户密码
     * @param staffId
     * @param newPassword
     * @return
     */
    ResponseEntity<?> updatePassword(String staffId, String newPassword);

    ResponseEntity<?> registerStaff(staff staff);

    ResponseEntity<?> deleteStaff(String staffId);
}
