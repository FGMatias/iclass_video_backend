package com.iclass.video.dto.response.device;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeviceInfoDTO {
    private Integer id;
    private String deviceName;
    private String deviceUsername;
    private String currentAreaName;
    private String deviceType;
    private Boolean isActive;
    private LocalDateTime lastLogin;
    private LocalDateTime lastSync;
}
