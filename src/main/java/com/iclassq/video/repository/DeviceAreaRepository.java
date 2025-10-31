package com.iclassq.video.repository;

import com.iclassq.video.entity.DeviceArea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceAreaRepository extends JpaRepository<DeviceArea, Integer> {
    @Query("SELECT da FROM DeviceArea da " +
            "JOIN FETCH da.area a " +
            "JOIN FETCH a.branch b " +
            "JOIN FETCH b.company c " +
            "JOIN FETCH da.user u " +
            "WHERE da.user.id = :userId")
    List<DeviceArea> findByUserIdWithDetails(Integer userId);

    List<DeviceArea> findByArea_Id(Integer areaId);
    List<DeviceArea> findByUser_Id(Integer userId);
    List<DeviceArea> findByDeviceIdentifier(String deviceIdentifier);
}
