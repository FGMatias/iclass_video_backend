package com.iclassq.video.dto.response.device;

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
    private Integer userId;
    private String username;
    private Integer areaId;
    private String areaName;
    private String branchName;
    private String companyName;
    private String deviceName;
    private String deviceIdentifier;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
