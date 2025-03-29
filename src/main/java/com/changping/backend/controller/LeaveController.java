package com.changping.backend.controller;

import com.changping.backend.DTO.LeaveRequestDTO;
import com.changping.backend.entity.LeaveRequest;
import com.changping.backend.jwt.util.JwtUtil;
import com.changping.backend.repository.StaffRepository;
import com.changping.backend.service.LeaveService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/leave")
public class LeaveController {
    private final LeaveService leaveService;
    public LeaveController(LeaveService leaveService) {
        this.leaveService = leaveService;
    }
    // **使用相对路径存储**
    @Value("${file.upload-dir}")
    private String uploadDir;

    @PostMapping("/submit")
    public ResponseEntity<Void> submitLeave(
            @RequestParam("file") MultipartFile file,  // 接收上传的文件
            @RequestParam("name") String name,         // 其他的表单数据
            @RequestParam("class_num") String classNum,
            @RequestParam("student_id") String studentId,
            @RequestParam("leave_type") String leaveType,
            @RequestParam("leave_date") String leaveDateStr,
            @RequestParam("remarks") String remarks,
            @RequestHeader("Authorization") String authorizationHeader  // 从请求头获取 token
    ) {
        Date leaveDate = Date.valueOf(leaveDateStr);    //处理参数
        String token = authorizationHeader.replace("Bearer ", "");    // 提取 token（去掉 "Bearer " 前缀）
        Map<String, Object> claims = JwtUtil.verifyToken(token, JwtUtil.DEFAULT_SECRET);
        String staffId = (String) claims.get("staffId");
        // 处理文件存储（可以将文件存储到服务器的特定目录）
        String filePath = file != null ? saveFile(file) : null;
        System.out.println("submitLeave: filePath" + filePath + " name" + name + " classNum" + classNum);
        // 创建 LeaveRequest 对象，并设置相关数据
        LeaveRequest leaveRequest = new LeaveRequest();
        leaveRequest.setName(name);
        leaveRequest.setClass_num(classNum);
        leaveRequest.setStudentId(studentId);
        leaveRequest.setLeave_type(leaveType);
        leaveRequest.setLeave_date(leaveDate);
        leaveRequest.setRemarks(remarks);
        leaveRequest.setStaffId(staffId);
        leaveRequest.setFile_path(filePath);

        // 将请假请求保存到数据库
        leaveService.postLeaveRequest(leaveRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/byStaffid")
    public List<LeaveRequestDTO> getLeavesByStaffId(@RequestParam String staffId) {
        System.out.println("getLeavesByStaffId");
        if (staffId == null || staffId.isEmpty()) {
            return null;
        }
        else return leaveService.getLeaveRequestById(staffId);
    }

    // 保存文件到服务器
    private String saveFile(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return null;  // 如果没有文件，可以选择返回 null 或者默认路径
            }
            File directory = new File(uploadDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            String fileName = file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir + fileName);
            Files.write(filePath, file.getBytes());
            return "uploadimage/" + fileName;
        } catch (IOException e) {
            e.printStackTrace();
            return null;  // 发生错误时返回 null
        }
    }
}
