package com.iclass.video.repository;

import com.iclass.video.entity.Area;
import com.iclass.video.entity.DeviceArea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeviceAreaRepository extends JpaRepository<DeviceArea, Integer> {
    @Query("SELECT da FROM DeviceArea da " +
            "JOIN FETCH da.area a " +
            "JOIN FETCH a.branch b " +
            "JOIN FETCH b.company c " +
            "WHERE da.device.id = :deviceId " +
            "AND da.removedAt IS NULL")
    Optional<DeviceArea> findCurrentAssignment(Integer deviceId);

    @Query("SELECT da FROM DeviceArea da " +
            "JOIN FETCH da.area a " +
            "JOIN FETCH da.assignedBy " +
            "WHERE da.device.id = :deviceId " +
            "ORDER BY da.assignedAt DESC")
    List<DeviceArea> findByDeviceIdOrderByAssignedAtDesc(Integer deviceId);

    @Query("SELECT da FROM DeviceArea da " +
            "JOIN FETCH da.device d " +
            "WHERE da.area.id = :areaId " +
            "ORDER BY da.assignedAt DESC")
    List<DeviceArea> findByAreaIdOrderByAssignedAtDesc(Integer areaId);

    @Query("SELECT da.device.id FROM DeviceArea da " +
            "WHERE da.area.branch.id = :branchId " +
            "AND da.isCurrent = true " +
            "AND da.removedAt IS NULL")
    List<Integer> findCurrentDevicesByBranchId(@Param("branchId") Integer branchId);

    @Query("SELECT da.device.id FROM DeviceArea da " +
            "WHERE da.area.id = :areaId " +
            "AND da.isCurrent = true " +
            "AND da.removedAt IS NULL")
    List<Integer> findCurrentDevicesByAreaId(@Param("areaId") Integer areaId);

    @Query("SELECT da FROM DeviceArea da " +
            "JOIN FETCH da.device d " +
            "JOIN FETCH d.deviceType " +
            "WHERE da.area.branch.id = :branchId " +
            "AND da.removedAt IS NULL")
    List<DeviceArea> findCurrentByBranchId(@Param("branchId") Integer branchId);

    @Query("SELECT da FROM DeviceArea da " +
            "JOIN FETCH da.device d " +
            "JOIN FETCH d.deviceType " +
            "WHERE da.area.id = :areaId " +
            "AND da.removedAt IS NULL")
    List<DeviceArea> findCurrentByAreaId(@Param("areaId") Integer areaId);
}
