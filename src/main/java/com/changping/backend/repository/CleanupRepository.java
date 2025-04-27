package com.changping.backend.repository;

import com.changping.backend.entity.cleanupRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CleanupRepository extends JpaRepository<cleanupRecord, Integer> {

}
