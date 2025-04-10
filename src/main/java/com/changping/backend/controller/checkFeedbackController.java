package com.changping.backend.controller;

import com.changping.backend.entity.feedback;
import com.changping.backend.service.FeedbackService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/checkFeedback")
public class checkFeedbackController {
    private final FeedbackService feedbackService;
    public checkFeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @GetMapping()
    List<feedback> checkFeedback(){
        return feedbackService.getAllFeedback();
    }

    @PostMapping("/addResponse")
    ResponseEntity<?> addResponse(@RequestParam int id, @RequestParam String response){
        return feedbackService.postResponse(id, response);
    }
}
