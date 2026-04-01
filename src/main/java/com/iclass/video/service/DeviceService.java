package com.iclass.video.service;

import com.iclass.video.dto.request.device.DeviceAssignAreaDTO;
import com.iclass.video.dto.request.device.CreateDeviceDTO;
import com.iclass.video.dto.request.device.UpdateDeviceDTO;
import com.iclass.video.dto.response.device.DeviceAuthResponseDTO;
import com.iclass.video.dto.response.device.DeviceResponseDTO;
import com.iclass.video.dto.response.device.DeviceSyncResponseDTO;
import com.iclass.video.entity.DeviceArea;

import java.util.List;

public interface DeviceService {
    List<DeviceResponseDTO> findAll();
    List<DeviceResponseDTO> findByAreaId(Integer areaId);
    List<DeviceResponseDTO> findByBranchId(Integer branchId);
    DeviceResponseDTO getDeviceWithCurrentArea(Integer id);
    DeviceResponseDTO create(CreateDeviceDTO dto, Integer adminUserId);
    DeviceResponseDTO update(Integer id, UpdateDeviceDTO dto);
    void delete(Integer id);
    void activate(Integer id);
    void deactivate(Integer id);
    void reassign(Integer id, DeviceAssignAreaDTO dto, Integer adminUserId);
    List<DeviceArea> getHistory(Integer id);
    DeviceSyncResponseDTO syncDevice(Integer id);
    DeviceAuthResponseDTO login(String deviceUsername, String devicePassword);
    void resetPassword(Integer id, String newPassword);
}
