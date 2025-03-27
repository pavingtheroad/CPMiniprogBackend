package com.changping.backend.controller;

import com.changping.backend.DTO.LeaveRequestDTO;
import com.changping.backend.entity.LeaveRequest;
import com.changping.backend.repository.StaffRepository;
import com.changping.backend.service.LeaveService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.Date;
import java.util.List;

@RestController
@RequestMapping("/leave")
public class LeaveController {
    private final LeaveService leaveService;
    public LeaveController(LeaveService leaveService) {
        this.leaveService = leaveService;
    }

    @PostMapping("/submit")
    public ResponseEntity<Void> submitLeave(
            @RequestParam("file") MultipartFile file,  // 接收上传的文件
            @RequestParam("name") String name,         // 其他的表单数据
            @RequestParam("class_num") String classNum,
            @RequestParam("student_id") String studentId,
            @RequestParam("leave_type") String leaveType,
            @RequestParam("leave_date") Date leaveDate,
            @RequestParam("remarks") String remarks,
            @RequestParam("staff_name") String staffName
    ) {
        System.out.println("submitLeave");
        // 处理文件存储（可以将文件存储到服务器的特定目录）
        String filePath = saveFile(file);

        // 创建 LeaveRequest 对象，并设置相关数据
        LeaveRequest leaveRequest = new LeaveRequest();
        leaveRequest.setName(name);
        leaveRequest.setClass_num(classNum);
        leaveRequest.setStudentId(studentId);
        leaveRequest.setLeave_type(leaveType);
        leaveRequest.setLeave_date(leaveDate);
        leaveRequest.setRemarks(remarks);
        leaveRequest.setStaffId(leaveService.getStaffIdByName(staffName));
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
        if (file.isEmpty()) {
            return null;  // 如果没有文件，可以选择返回 null 或者默认路径
        }

        try {
            // 获取上传目录的绝对路径
            String uploadDirectory = Paths.get("src", "main", "resources", "static", "uploadimage").toString();
            File directory = new File(uploadDirectory);
            if (!directory.exists()) {
                directory.mkdirs();  // 如果目录不存在，则创建目录
            }

            // 获取文件名并拼接成完整路径
            String fileName = file.getOriginalFilename();
            String filePath = uploadDirectory + File.separator + fileName;

            // 保存文件
            File dest = new File(filePath);
            file.transferTo(dest);

            // 返回文件的相对路径
            return "/static/uploadimage/" + fileName;
        } catch (IOException e) {
            e.printStackTrace();
            return null;  // 发生错误时返回 null
        }
    }
}
