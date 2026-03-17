package com.iclass.video.mapper;

import com.iclass.video.dto.request.config.UpdateSystemConfigDTO;
import com.iclass.video.dto.response.config.SystemConfigResponseDTO;
import com.iclass.video.entity.SystemConfig;
import com.iclass.video.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SystemConfigMapper {

    public void updateEntity(SystemConfig config, UpdateSystemConfigDTO dto, User updatedBy) {
        if (dto.getConfigValue() != null) {
            config.setConfigValue(dto.getConfigValue());
        }
        if (updatedBy != null) {
            config.setUpdatedBy(updatedBy);
        }
    }

    public SystemConfigResponseDTO toResponseDTO(SystemConfig config) {
        return SystemConfigResponseDTO.builder()
                .id(config.getId())
                .configKey(config.getConfigKey())
                .configValue(config.getConfigValue())
                .configType(config.getConfigType())
                .defaultValue(config.getDefaultValue())
                .description(config.getDescription())
                .validationRule(config.getValidationRule())
                .displayOrder(config.getDisplayOrder())
                .updatedByUser(config.getUpdatedBy() != null ? config.getUpdatedBy().getId() : null)
                .updatedAt(config.getUpdatedAt())
                .build();
    }

    public List<SystemConfigResponseDTO> toResponseDTOList(List<SystemConfig> configs) {
        return configs.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }
}
