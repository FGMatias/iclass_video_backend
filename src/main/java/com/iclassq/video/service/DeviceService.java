package com.iclassq.video.service;

import com.iclassq.video.dto.request.device.DeviceAssignAreaDTO;
import com.iclassq.video.dto.request.device.RegisterDeviceDTO;
import com.iclassq.video.dto.response.device.DeviceAuthResponseDTO;
import com.iclassq.video.dto.response.device.DeviceRegisterResponseDTO;
import com.iclassq.video.dto.response.device.DeviceResponseDTO;
import com.iclassq.video.entity.DeviceArea;

import java.util.List;

public interface DeviceService {
    DeviceRegisterResponseDTO register(RegisterDeviceDTO dto, Integer adminUserId);
    DeviceAuthResponseDTO login(String deviceUsername, String devicePassword);
    void reassign(Integer deviceId, DeviceAssignAreaDTO dto, Integer adminUserId);
    DeviceResponseDTO getDeviceWithCurrentArea(Integer deviceId);
    List<DeviceArea> getHistory(Integer deviceId);
    void deactivate(Integer deviceId);
    void updateLastSync(Integer deviceId);
}
