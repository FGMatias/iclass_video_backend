package com.iclass.video.dto.response.area;

import com.iclass.video.dto.response.device.DeviceInfoDTO;
import com.iclass.video.dto.response.video.VideoSimpleDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AreaDetailDTO {
    private Integer id;
    private Integer branchId;
    private String branchName;
    private Integer companyId;
    private String companyName;
    private String name;
    private String description;
    private Boolean isActive;
    private Integer totalVideos;
    private Integer totalDevices;
    private Integer totalDuration;
    private List<VideoSimpleDTO> playlist;
    private List<DeviceInfoDTO> devices;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
