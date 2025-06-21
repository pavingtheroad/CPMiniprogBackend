package com.changping.backend.DTO;

import com.changping.backend.entity.staff;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;

@Data
public class StaffDTO {
    private String staffId;
    private String name;
    private String permission;

    public StaffDTO(String staffId, String name, String permission) {
        this.staffId = staffId;
        this.name = name;
        this.permission = permission;
    }

    public static StaffDTO fromStaff(staff staff) {
        return new StaffDTO(
                staff.getStaffId(),
                staff.getName(),
                staff.getPermission()
        );
    }
}
