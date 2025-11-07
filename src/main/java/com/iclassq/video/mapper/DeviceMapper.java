package com.iclassq.video.mapper;

import com.iclassq.video.dto.request.device.CreateDeviceDTO;
import com.iclassq.video.dto.request.device.RegisterDeviceDTO;
import com.iclassq.video.dto.response.device.DeviceAuthResponseDTO;
import com.iclassq.video.dto.response.device.DeviceRegisterResponseDTO;
import com.iclassq.video.dto.response.device.DeviceResponseDTO;
import com.iclassq.video.dto.response.video.VideoSimpleDTO;
import com.iclassq.video.entity.*;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DeviceMapper {

    public Device toEntity(
            RegisterDeviceDTO dto,
            DeviceType deviceType,
            User configuredBy,
            String hashedPassword
    ) {
        return Device.builder()
                .user(configuredBy)
                .deviceType(deviceType)
                .deviceName(dto.getDeviceName())
                .deviceIdentifier(dto.getDeviceIdentifier())
                .deviceUsername(dto.getDeviceUsername())
                .devicePassword(hashedPassword)
                .isActive(true)
                .build();
    }

    public DeviceRegisterResponseDTO toRegisterResponseDTO(
            Device device,
            String rawPassword,
            Area area
    ) {
        return DeviceRegisterResponseDTO.builder()
                .id(device.getId())
                .deviceName(device.getDeviceName())
                .deviceUsername(device.getDeviceUsername())
                .devicePassword(rawPassword)
                .deviceType(device.getDeviceType().getName())
                .areaId(area.getId())
                .areaName(area.getName())
                .message("Dispositivo registrado correctamente.")
                .build();
    }

    public DeviceResponseDTO toResponseDTO(
            Device device,
            DeviceArea currentAssignment
    ) {
        DeviceResponseDTO.DeviceResponseDTOBuilder builder = DeviceResponseDTO.builder()
                .id(device.getId())
                .deviceName(device.getDeviceName())
                .deviceIdentifier(device.getDeviceIdentifier())
                .deviceUsername(device.getDeviceUsername())
                .deviceType(device.getDeviceType().getName())
                .isActive(device.getIsActive())
                .lastLogin(device.getLastLogin())
                .lastSync(device.getLastSync())
                .createdAt(device.getCreatedAt());

        if (currentAssignment != null) {
            Area area = currentAssignment.getArea();
            Branch branch = area.getBranch();
            Company company = branch.getCompany();

            builder
                    .currentAreaId(area.getId())
                    .currentAreaName(area.getName())
                    .currentBranchName(branch.getName())
                    .currentCompanyName(company.getName());
        }

        if (device.getUser() != null) {
            builder.configuredByUsername(device.getUser().getUsername());
        }

        return builder.build();
    }

    public DeviceAuthResponseDTO toAuthResponseDTO(
            Device device,
            DeviceArea currentAssignment,
            List<AreaVideo> areaVideos,
            String token
    ) {
        Area area = currentAssignment.getArea();
        Branch branch = area.getBranch();
        Company company = branch.getCompany();

        List<VideoSimpleDTO> playlist = areaVideos.stream()
                .sorted(Comparator.comparing(AreaVideo::getOrden))
                .map(av -> VideoSimpleDTO.builder()
                        .id(av.getVideo().getId())
                        .name(av.getVideo().getName())
                        .urlVideo(av.getVideo().getUrlVideo())
                        .thumbnail(av.getVideo().getThumbnail())
                        .duration(av.getVideo().getDuration())
                        .orden(av.getOrden())
                        .build())
                .collect(Collectors.toList());

        return DeviceAuthResponseDTO.builder()
                .token(token)
                .type("Bearer")
                .id(device.getId())
                .deviceName(device.getDeviceName())
                .deviceUsername(device.getDeviceUsername())
                .deviceType(device.getDeviceType().getName())
                .areaId(area.getId())
                .areaName(area.getName())
                .branchId(branch.getId())
                .branchName(branch.getName())
                .companyId(company.getId())
                .companyName(company.getName())
                .config(DeviceAuthResponseDTO.DeviceConfig.builder()
                        .autoSyncInterval(21600)
                        .autoPlay(true)
                        .loopPlayList(true)
                        .volume(80)
                        .build())
                .playlist(playlist)
                .lastSync(device.getLastSync())
                .build();
    }
}
