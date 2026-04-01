package com.iclass.video.repository;

import com.iclass.video.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Integer> {
    Optional<Device> findByDeviceUsername(String deviceUsername);

    Optional<Device> findByDeviceIdentifier(String deviceIdentifier);

    Boolean existsByDeviceUsername(String deviceUsername);

    Boolean existsByDeviceIdentifier(String deviceIdentifier);

    @Query("SELECT d FROM Device d " +
            "WHERE d.deviceUsername = :username " +
            "AND d.isActive = true")
    Optional<Device> findActiveByUsername(String username);

    List<Device> findByBranchId(Integer branchId);
}
