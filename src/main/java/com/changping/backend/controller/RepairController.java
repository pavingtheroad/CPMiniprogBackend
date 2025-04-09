package com.changping.backend.controller;

import com.changping.backend.DTO.RepairDTO;
import com.changping.backend.entity.repair;
import com.changping.backend.jwt.util.JwtUtil;
import com.changping.backend.service.RepairService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/repair")
public class RepairController {
    @Value("${file.uploadRepair-dir}")
    private String uploadRepairDir;

    private final RepairService repairService;
    public RepairController(RepairService repairService) {
        this.repairService = repairService;
    }
    @GetMapping("/myApply")
    public List<repair> getMyApply(@RequestHeader("Authorization") String authorizationHeader){
        String token = authorizationHeader.replace("Bearer ", "");    // 提取 token（去掉 "Bearer " 前缀）
        Map<String, Object> claims = JwtUtil.verifyToken(token, JwtUtil.DEFAULT_SECRET);
        String staffId = (String) claims.get("staffId");
        System.out.println("myApply:staffId"+staffId);
        return repairService.getRepairListByStaffId(staffId);
    }

    @PostMapping("/post")
    public ResponseEntity<?> postRepair(
            @RequestParam("file") MultipartFile file,
            RepairDTO repairDTO,
            @RequestHeader("Authorization") String authorizationHeader){
        String filePath = file != null ? saveFile(file) : null;

        String token = authorizationHeader.replace("Bearer ", "");    // 提取 token（去掉 "Bearer " 前缀）
        Map<String, Object> claims = JwtUtil.verifyToken(token, JwtUtil.DEFAULT_SECRET);
        String staffId = (String) claims.get("staffId");
        repairDTO.setStaffId(staffId);
        repair repairEntity = repairDTO.transToRepair(filePath);
        repairEntity.setHandle(false);
        return repairService.postRepairApply(repairEntity);
    }

    private String saveFile(MultipartFile file){
        try{
            if(file.isEmpty()){
               return null;
            }
            File directory = new File(uploadRepairDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            String fileName = file.getOriginalFilename();
            Path filePath = Paths.get(uploadRepairDir + fileName);
            Files.write(filePath, file.getBytes());
            return "uploadRepair/" + fileName;
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }
}
