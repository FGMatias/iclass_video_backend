package com.iclass.video.dto.response.config;

import com.iclass.video.enums.ConfigType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SystemConfigResponseDTO {
    private Integer id;
    private String configKey;
    private String configValue;
    private ConfigType configType;
    private String defaultValue;
    private String description;
    private String validationRule;
    private Integer displayOrder;
    private Integer updatedByUser;
    private LocalDateTime updatedAt;
}
