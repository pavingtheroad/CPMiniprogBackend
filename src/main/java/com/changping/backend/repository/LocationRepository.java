package com.changping.backend.repository;

import com.changping.backend.entity.location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<location, Integer> {
    location findByPlaces(String places);
}
