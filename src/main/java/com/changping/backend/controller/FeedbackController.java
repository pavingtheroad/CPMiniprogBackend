package com.changping.backend.controller;

import com.changping.backend.entity.feedback;
import com.changping.backend.jwt.util.JwtUtil;
import com.changping.backend.service.feedback.FeedbackService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/feedback")
public class FeedbackController {
    private final FeedbackService feedbackService;
    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @GetMapping("/getByStaff")
    public List<feedback> getByStaffId(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");
        Map<String, Object> claims = JwtUtil.verifyToken(token, JwtUtil.DEFAULT_SECRET);
        String staffId = (String) claims.get("staffId");
        return feedbackService.getFeedbackByStaffId(staffId);
    }

    @PostMapping("/addFeedback")
    public ResponseEntity<?> addFeedback(@RequestHeader("Authorization") String authorizationHeader,@RequestBody feedback feedback) {
        String token = authorizationHeader.replace("Bearer ", "");
        Map<String, Object> claims = JwtUtil.verifyToken(token, JwtUtil.DEFAULT_SECRET);
        String staffId = (String) claims.get("staffId");
        feedback.setStaffId(staffId);
        return feedbackService.postFeedback(feedback);
    }
}
