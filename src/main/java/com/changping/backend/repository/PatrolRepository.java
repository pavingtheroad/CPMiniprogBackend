package com.changping.backend.repository;

import com.changping.backend.entity.patrol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PatrolRepository extends JpaRepository<patrol, Integer> {
    List<patrol> findByStaffIdAndCheckTimeBetween(String staffId, LocalDateTime start, LocalDateTime end);

    patrol findByStaffIdAndLocation(String staffId, String location);
}
