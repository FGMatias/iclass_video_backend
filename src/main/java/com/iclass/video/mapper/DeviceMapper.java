package com.iclass.video.mapper;

import com.iclass.video.dto.request.device.CreateDeviceDTO;
import com.iclass.video.dto.request.device.UpdateDeviceDTO;
import com.iclass.video.dto.response.device.DeviceAuthResponseDTO;
import com.iclass.video.dto.response.device.DeviceResponseDTO;
import com.iclass.video.dto.response.device.DeviceSyncResponseDTO;
import com.iclass.video.dto.response.video.VideoSimpleDTO;
import com.iclass.video.entity.*;
import com.iclass.video.entity.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DeviceMapper {

    public Device toEntity(
            CreateDeviceDTO dto,
            DeviceType deviceType,
            User configuredBy,
            String hashedPassword
    ) {
        return Device.builder()
                .user(configuredBy)
                .deviceType(deviceType)
                .deviceName(dto.getDeviceName())
                .deviceUsername(dto.getDeviceUsername())
                .devicePassword(hashedPassword)
                .isActive(true)
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

    public DeviceResponseDTO toResponseDTO(Device device) {
        return DeviceResponseDTO.builder()
                .id(device.getId())
                .deviceName(device.getDeviceName())
                .deviceIdentifier(device.getDeviceIdentifier())
                .deviceUsername(device.getDeviceUsername())
                .deviceType(device.getDeviceType().getName())
                .isActive(device.getIsActive())
                .lastLogin(device.getLastLogin())
                .lastSync(device.getLastSync())
                .createdAt(device.getCreatedAt())
                .configuredByUsername(device.getUser() != null ? device.getUser().getUsername() : null)
                .build();
    }

    public void updateEntity(
            Device device,
            UpdateDeviceDTO dto,
            DeviceType newDeviceType
    ) {
        if (dto.getDeviceUsername() != null) device.setDeviceUsername(dto.getDeviceUsername());
        if (dto.getDeviceName() != null) device.setDeviceName(dto.getDeviceName());
        if (newDeviceType != null) device.setDeviceType(newDeviceType);
        if (dto.getIsActive() != null) device.setIsActive(dto.getIsActive());
    }

    public DeviceAuthResponseDTO toAuthResponseDTO(
            Device device,
            DeviceArea currentAssignment,
            List<AreaVideo> areaVideos,
            String token,
            Integer volume,
            Integer syncInterval,
            Boolean autoPlay,
            Boolean loopPlaylist
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
                        .fileSize(av.getVideo().getFileSize())
                        .checksum(av.getVideo().getChecksum())
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
                        .autoSyncInterval(syncInterval)
                        .autoPlay(autoPlay)
                        .loopPlayList(loopPlaylist)
                        .volume(volume)
                        .build())
                .playlist(playlist)
                .lastSync(device.getLastSync())
                .build();
    }

    public List<DeviceResponseDTO> toResponseDTOList(List<Device> devices) {
        return devices.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public DeviceSyncResponseDTO toSyncResponseDTO(
            List<AreaVideo> areaVideos,
            Integer volume,
            Integer syncInterval,
            Boolean autoPlay,
            Boolean loopPlaylist,
            List<PendingSyncEvent> pendingEvents
    ) {
        List<DeviceSyncResponseDTO.VideoSyncInfo> playlist = areaVideos.stream()
                .sorted(Comparator.comparing(AreaVideo::getOrden))
                .map(av -> DeviceSyncResponseDTO.VideoSyncInfo.builder()
                        .id(av.getVideo().getId())
                        .name(av.getVideo().getName())
                        .urlVideo(av.getVideo().getUrlVideo())
                        .thumbnail(av.getVideo().getThumbnail())
                        .duration(av.getVideo().getDuration())
                        .orden(av.getOrden())
                        .fileSize(av.getVideo().getFileSize())
                        .checksum(av.getVideo().getChecksum())
                        .build())
                .collect(Collectors.toList());

        DeviceSyncResponseDTO.DeviceConfigInfo config = DeviceSyncResponseDTO.DeviceConfigInfo.builder()
                .autoSyncInterval(syncInterval)
                .autoPlay(autoPlay)
                .loopPlayList(loopPlaylist)
                .volume(volume)
                .build();

        List<DeviceSyncResponseDTO.EventInfo> events = pendingEvents.stream()
                .map(e -> DeviceSyncResponseDTO.EventInfo.builder()
                        .eventId(e.getId())
                        .eventType(e.getEventType().name())
                        .createdAt(e.getCreatedAt())
                        .message(buildEventMessage(e))
                        .build())
                .collect(Collectors.toList());

        return DeviceSyncResponseDTO.builder()
                .serverTime(LocalDateTime.now())
                .playlist(playlist)
                .config(config)
                .pendingEvents(events)
                .build();
    }

    private String buildEventMessage(PendingSyncEvent event) {
        switch (event.getEventType()) {
            case CONFIG_CHANGED:
                return "La configuración ha sido actualizada";
            case PLAYLIST_CHANGED:
                return "La playlist ha sido modificada";
            case DEVICE_REASSIGNED:
                return "El dispositivo fue reasignado a otra área";
            case DEVICE_DISABLED:
                return "El dispositivo ha sido desactivado";
            default:
                return "Evento desconocido";
        }
    }
}
