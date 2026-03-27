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
public class DeviceResponseDTO {
    private Integer id;
    private String deviceName;
    private String deviceIdentifier;
    private String deviceUsername;
    private String deviceType;
    private Boolean isActive;

    private Integer currentAreaId;
    private String currentAreaName;
    private Integer currentBranchId;
    private String currentBranchName;
    private Integer currentCompanyId;
    private String currentCompanyName;
    private LocalDateTime assignedAt;

    private String configuredByUsername;
    private LocalDateTime lastLogin;
    private LocalDateTime lastSync;
    private LocalDateTime createdAt;
}
