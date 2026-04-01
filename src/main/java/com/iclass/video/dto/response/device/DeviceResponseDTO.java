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
    private Integer companyId;
    private String companyName;
    private Integer branchId;
    private String branchName;
    private Integer currentAreaId;
    private String currentAreaName;
    private LocalDateTime assignedAt;
    private String configuredByUsername;
    private LocalDateTime lastLogin;
    private LocalDateTime lastSync;
    private LocalDateTime createdAt;
}
