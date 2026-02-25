package com.iclass.video.repository;

import com.iclass.video.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoRepository extends JpaRepository<Video, Integer> {
    List<Video> findByCompany_Id(Integer companyId);
    Boolean existsByName(String name);
} 
