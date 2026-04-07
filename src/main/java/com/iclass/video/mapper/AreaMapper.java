package com.iclass.video.mapper;

import com.iclass.video.dto.request.area.CreateAreaDTO;
import com.iclass.video.dto.request.area.UpdateAreaDTO;
import com.iclass.video.dto.response.area.AreaDetailDTO;
import com.iclass.video.dto.response.area.AreaResponseDTO;
import com.iclass.video.dto.response.device.DeviceInfoDTO;
import com.iclass.video.dto.response.video.VideoSimpleDTO;
import com.iclass.video.entity.Area;
import com.iclass.video.entity.AreaVideo;
import com.iclass.video.entity.Branch;
import com.iclass.video.entity.DeviceArea;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AreaMapper {

    public Area toEntity(CreateAreaDTO dto, Branch branch) {
        return Area.builder()
                .branch(branch)
                .name(dto.getName())
                .description(dto.getDescription())
                .isActive(true)
                .build();
    }

    public void updateEntity(Area area, UpdateAreaDTO dto) {
        if (dto.getName() != null) area.setName(dto.getName());
        if (dto.getDescription() != null) area.setDescription(dto.getDescription());
        if (dto.getIsActive() != null) area.setIsActive(dto.getIsActive());
    }

    public AreaResponseDTO toResponseDTO(Area area) {
        return AreaResponseDTO.builder()
                .id(area.getId())
                .branchId(area.getBranch().getId())
                .branchName(area.getBranch().getName())
                .companyId(area.getBranch().getCompany().getId())
                .companyName(area.getBranch().getCompany().getName())
                .name(area.getName())
                .description(area.getDescription())
                .isActive(area.getIsActive())
                .createdAt(area.getCreatedAt())
                .updatedAt(area.getUpdatedAt())
                .build();
    }

    public AreaDetailDTO toDetailDTO(
            Area area,
            List<AreaVideo> areaVideos,
            List<DeviceArea> currentDevices
    ) {
        List<VideoSimpleDTO> playlist = areaVideos.stream()
                .sorted(Comparator.comparing(AreaVideo::getOrden))
                .map(av -> VideoSimpleDTO.builder()
                        .id(av.getVideo().getId())
                        .name(av.getVideo().getName())
                        .urlVideo(av.getVideo().getUrlVideo())
                        .fileExtension(av.getVideo().getFileExtension())
                        .thumbnail(av.getVideo().getThumbnail())
                        .duration(av.getVideo().getDuration())
                        .orden(av.getOrden())
                        .build())
                .collect(Collectors.toList());

        List<DeviceInfoDTO> devices = currentDevices.stream()
                .map(da -> DeviceInfoDTO.builder()
                        .id(da.getDevice().getId())
                        .deviceName(da.getDevice().getDeviceName())
                        .deviceUsername(da.getDevice().getDeviceUsername())
                        .currentAreaName(da.getArea().getName())
                        .deviceType(da.getDevice().getDeviceType().getName())
                        .isActive(da.getDevice().getIsActive())
                        .lastLogin(da.getDevice().getLastLogin())
                        .lastSync(da.getDevice().getLastSync())
                        .build())
                .collect(Collectors.toList());

        Integer totalDuration = playlist.stream()
                .mapToInt(v -> v.getDuration() != null ? v.getDuration() : 0)
                .sum();

        Branch branch = area.getBranch();

        return AreaDetailDTO.builder()
                .id(area.getId())
                .branchId(branch.getId())
                .branchName(branch.getName())
                .companyId(branch.getCompany().getId())
                .companyName(branch.getCompany().getName())
                .name(area.getName())
                .description(area.getDescription())
                .isActive(area.getIsActive())
                .totalVideos(playlist.size())
                .totalDevices(devices.size())
                .totalDuration(totalDuration)
                .playlist(playlist)
                .devices(devices)
                .createdAt(area.getCreatedAt())
                .updatedAt(area.getUpdatedAt())
                .build();
    }

    public List<AreaResponseDTO> toResponseDTOList(List<Area> areas) {
        return areas.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }
}
