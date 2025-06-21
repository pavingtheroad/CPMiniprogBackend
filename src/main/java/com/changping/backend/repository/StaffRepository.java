package com.changping.backend.repository;

import com.changping.backend.entity.staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StaffRepository extends JpaRepository<staff, Integer> {
    staff findByName(String name);
    staff findByStaffId(String id);
    List<staff> findAllByName(String name);
//    /**
//     * 进行分页查询，提供给管理员页面
//     * @param pageable
//     * @return
//     */
//    @Query("SELECT s FROM staff s")
//    Page<staff> findAll(Pageable pageable);
    void deleteByStaffId(String staffId);


}
