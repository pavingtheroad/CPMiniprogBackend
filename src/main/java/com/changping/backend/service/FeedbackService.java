package com.changping.backend.service;

import com.changping.backend.entity.feedback;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface FeedbackService {
    List<feedback> getFeedbackByStaffId(String staffId);

    ResponseEntity<?> postFeedback(feedback feedback);

    ResponseEntity<?> postResponse(Integer id, String response);

    List<feedback> getAllFeedback();
}
