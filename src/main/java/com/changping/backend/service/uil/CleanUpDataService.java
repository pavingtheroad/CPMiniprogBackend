package com.changping.backend.service.uil;

import com.changping.backend.entity.cleanupRecord;
import com.changping.backend.repository.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

// 负责进行数据的定期删除，清空feedback,leave_request,patrol,repair表
@Service
public class CleanUpDataService {
    private final CleanupRepository cleanupRepository;
    private FeedbackRepository feedbackRepository;
    private LeaveRepository leaveRepository;
    private RepairRepository repairRepository;
    private PatrolRepository patrolRepository;
    public CleanUpDataService(FeedbackRepository feedbackRepository, LeaveRepository leaveRepository, RepairRepository repairRepository, PatrolRepository patrolRepository, CleanupRepository cleanupRepository) {
        this.cleanupRepository = cleanupRepository;
    }{
        this.feedbackRepository = feedbackRepository;
        this.leaveRepository = leaveRepository;
        this.repairRepository = repairRepository;
        this.patrolRepository = patrolRepository;
    }

    @Value("${file.uploadRepair-dir}")
    private String uploadRepairDir;
    @Value("${file.upload-dir}")
    private String uploadDir;

// 请假表&报修表15天一清除，反馈表&巡逻表30天一清
    @Scheduled(cron = "0 0 0 * * ?")
    public void cleanUpData() {
        LocalDateTime now = LocalDateTime.now();
        cleanUpDataForRecord(1001, 15, leaveRepository, repairRepository, now);
        cleanUpDataForRecord(1002, 30, feedbackRepository, patrolRepository, now);
    }

    private void cleanUpDataForRecord(int recordId, long daysThreshold, JpaRepository<?,Integer> repo1, JpaRepository<?,Integer> repo2, LocalDateTime now) {
        Optional<cleanupRecord> recordOpt = cleanupRepository.findById(recordId);

        if (recordOpt.isPresent()) {
            cleanupRecord record = recordOpt.get();
            if (Duration.between(record.getLastCleanupTime(), now).toDays() >= daysThreshold) {
                repo1.deleteAll();  // 清空数据
                repo2.deleteAll();
                record.setLastCleanupTime(now);  // 更新清空时间
                cleanupRepository.save(record);

                cleanUpFiles(uploadDir);
                cleanUpFiles(uploadRepairDir);
            }
        } else {
            cleanupRecord record = new cleanupRecord();
            record.setId(recordId);
            record.setLastCleanupTime(now);
            cleanupRepository.save(record);
        }
    }

    private void cleanUpFiles(String directoryPath) {
        File directory = new File(directoryPath);
        cleanupDirectory(directory);  // 调用递归方法清空文件夹
    }

    private void cleanupDirectory(File directory) {
        if (directory.exists() && directory.isDirectory()) {
            // 获取目录中的所有文件和子目录
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        // 如果是目录，递归清空子目录
                        cleanupDirectory(file);
                    }
                    file.delete(); // 删除文件
                }
            }
        }
    }
}
