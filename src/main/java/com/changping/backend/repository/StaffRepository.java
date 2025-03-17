package com.changping.backend.repository;

import com.changping.backend.entity.staff;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StaffRepository extends JpaRepository<staff, Integer> {
    staff findByName(String name);
}
