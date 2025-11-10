package com.iclassq.video.repository;

import com.iclassq.video.entity.Area;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AreaRepository extends JpaRepository<Area, Integer> {
    Boolean existsByName(String name);
}
