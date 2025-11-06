package com.iclassq.video.dto.response.device;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeviceRegisterResponseDTO {
    private Integer id;
    private String deviceName;
    private String deviceUsername;
    private String devicePassword;
    private String deviceType;
    private Integer areaId;
    private String areaName;
    private String message;
}
