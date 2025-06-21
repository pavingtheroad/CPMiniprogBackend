package com.changping.backend.service.feedback;

import com.changping.backend.entity.feedback;
import com.changping.backend.repository.FeedbackRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class FeedbackServiceImpl implements FeedbackService {
    private final FeedbackRepository feedbackRepository;
    public FeedbackServiceImpl(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    @Override
    public List<feedback> getFeedbackByStaffId(String staffId) {
        try {
            return feedbackRepository.findByStaffId(staffId);
        }catch (Exception e){
            log.error("通过staffId获取反馈列表失败：",e);
            return null;
        }
    }

    @Override
    public ResponseEntity<?> postFeedback(feedback feedback) {
        try{
            feedbackRepository.save(feedback);
            return ResponseEntity.ok().build();
        }catch (Exception e){
            log.error("上传反馈出错：",e);
            return ResponseEntity.badRequest().build();
        }
    }

    @Override
    public ResponseEntity<?> postResponse(Integer id, String response) {
        try{
            feedback feedback = feedbackRepository.findById(id).get();
            feedback.setResponse(response);
            feedbackRepository.save(feedback);
            return ResponseEntity.ok().build();
        }catch (Exception e){
            log.error("上传回复出错：",e);
            return ResponseEntity.badRequest().build();
        }
    }

    @Override
    public List<feedback> getAllFeedback() {
        return feedbackRepository.findAll();
    }


}
