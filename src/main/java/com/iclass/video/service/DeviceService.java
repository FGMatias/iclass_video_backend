package com.iclass.video.service;

import com.iclass.video.dto.request.auth.LoginDTO;
import com.iclass.video.dto.request.device.DeviceAssignAreaDTO;
import com.iclass.video.dto.request.device.CreateDeviceDTO;
import com.iclass.video.dto.request.device.UpdateDeviceDTO;
import com.iclass.video.dto.response.device.DeviceAuthResponseDTO;
import com.iclass.video.dto.response.device.DeviceInfoDTO;
import com.iclass.video.dto.response.device.DeviceResponseDTO;
import com.iclass.video.dto.response.device.DeviceSyncResponseDTO;
import com.iclass.video.entity.DeviceArea;

import java.util.List;

public interface DeviceService {
    List<DeviceInfoDTO> findAll();
    List<DeviceInfoDTO> findByAreaId(Integer areaId);
    List<DeviceInfoDTO> findByBranchId(Integer branchId);
    DeviceResponseDTO getDeviceWithCurrentArea(Integer id);
    DeviceResponseDTO create(CreateDeviceDTO dto, Integer adminUserId);
    DeviceResponseDTO update(Integer id, UpdateDeviceDTO dto);
    void delete(Integer id);
    void activate(Integer id);
    void deactivate(Integer id);
    void reassign(Integer id, DeviceAssignAreaDTO dto, Integer adminUserId);
    List<DeviceArea> getHistory(Integer id);
    DeviceSyncResponseDTO syncDevice(Integer id, String deviceUsername);
    DeviceAuthResponseDTO login(LoginDTO dto);
    void resetPassword(Integer id, String newPassword);
}
