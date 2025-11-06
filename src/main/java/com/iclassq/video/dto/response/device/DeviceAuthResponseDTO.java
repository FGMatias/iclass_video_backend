package com.iclassq.video.dto.response.device;

import com.iclassq.video.dto.response.video.VideoSimpleDTO;
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
public class DeviceAuthResponseDTO {
    private String token;
    private String type = "Bearer";
    private Integer id;
    private String deviceName;
    private String deviceUsername;
    private String deviceType;
    private Integer areaId;
    private String areaName;
    private Integer branchId;
    private String branchName;
    private Integer companyId;
    private String companyName;
    private DeviceConfig config;
    private List<VideoSimpleDTO > playlist;
    private LocalDateTime lastSync;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DeviceConfig {
        private Integer autoSyncInterval;
        private Boolean autoPlay;
        private Boolean loopPlayList;
        private Integer volume;
    }
}
