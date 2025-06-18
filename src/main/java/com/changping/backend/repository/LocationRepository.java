package com.changping.backend.repository;

import com.changping.backend.entity.location;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<location, Integer> {
    @Query("SELECT l.location_id from location l where l.places = :places")
    Integer findLocationIdByPlaces(@Param("places") String places);
}
