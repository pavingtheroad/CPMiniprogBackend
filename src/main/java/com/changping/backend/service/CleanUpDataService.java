package com.changping.backend.service;

import com.changping.backend.entity.cleanupRecord;
import com.changping.backend.repository.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

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
            }
        } else {
            cleanupRecord record = new cleanupRecord();
            record.setId(recordId);
            record.setLastCleanupTime(now);
            cleanupRepository.save(record);
        }
    }
}
