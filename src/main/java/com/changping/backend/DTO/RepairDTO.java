package com.changping.backend.DTO;

import com.changping.backend.entity.repair;
import lombok.Data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.Date;

@Data
public class RepairDTO {
    private String name;
    private String repairType;
    private String repairLocation;
    private String remarks;
    private String staffId;
    private String repairDate;

    public repair transToRepair(String filePath){
        repair entity = new repair();
        entity.setName(this.name);
        entity.setRepairType(this.repairType);
        entity.setRepairLocation(this.repairLocation);
        entity.setRemarks(this.remarks);
        entity.setStaffId(this.staffId);
        entity.setFilePath(filePath);
        Date parsedDate = Date.valueOf(this.repairDate);
        entity.setRepairDate(parsedDate);
        return entity;
    }
}
