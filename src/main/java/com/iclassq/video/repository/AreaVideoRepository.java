package com.iclassq.video.repository;

import com.iclassq.video.entity.AreaVideo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AreaVideoRepository extends JpaRepository<AreaVideo, Integer> {
    @Query("SELECT av FROM AreaVideo av " +
            "JOIN FETCH av.video v " +
            "WHERE av.area.id = :areaId " +
            "ORDER BY av.orden ASC")
    List<AreaVideo> findByAreaWithVideos(@Param("areaId") Integer areaId);

    List<AreaVideo> findByArea_Id(Integer areaId);

    void deleteByArea_Id(Integer areaId);
}
