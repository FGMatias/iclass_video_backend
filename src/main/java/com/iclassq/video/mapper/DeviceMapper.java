package com.iclassq.video.mapper;

import com.iclassq.video.dto.request.device.CreateDeviceDTO;
import com.iclassq.video.dto.response.device.DeviceResponseDTO;
import com.iclassq.video.entity.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DeviceMapper {

    public DeviceArea toEntity(CreateDeviceDTO dto, User user, Area area) {
        return DeviceArea.builder()
                .user(user)
                .area(area)
                .deviceName(dto.getDeviceName())
                .deviceIdentifier(dto.getDeviceIdentifier())
                .build();
    }

    public DeviceResponseDTO toResponseDTO(DeviceArea deviceArea) {
        Area area = deviceArea.getArea();
        Branch branch = area.getBranch();
        Company company = branch.getCompany();
        User user = deviceArea.getUser();

        return DeviceResponseDTO.builder()
                .id(deviceArea.getId())
                .userId(user.getId())
                .username(user.getUsername())
                .areaId(area.getId())
                .areaName(area.getName())
                .branchName(branch.getName())
                .companyName(company.getName())
                .deviceName(deviceArea.getDeviceName())
                .deviceIdentifier(deviceArea.getDeviceIdentifier())
                .createdAt(deviceArea.getCreatedAt())
                .updatedAt(deviceArea.getUpdatedAt())
                .build();
    }

    public List<DeviceResponseDTO> toResponseDTOList(List<DeviceArea> devices) {
        return devices.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }
}
