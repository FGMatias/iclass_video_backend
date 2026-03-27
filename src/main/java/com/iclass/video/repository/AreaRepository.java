package com.iclass.video.repository;

import com.iclass.video.entity.Area;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AreaRepository extends JpaRepository<Area, Integer> {
    Boolean existsByName(String name);
    List<Area> findByBranchId(Integer branchId);
}
