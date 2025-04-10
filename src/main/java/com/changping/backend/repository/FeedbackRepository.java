package com.changping.backend.repository;

import com.changping.backend.entity.feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<feedback, Integer> {
    List<feedback> findByStaffId(String staffId);

    feedback findById(int id);
}
