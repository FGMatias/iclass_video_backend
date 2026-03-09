package com.iclass.video.mapper;

import com.iclass.video.dto.request.config.UpdateBranchConfigDTO;
import com.iclass.video.dto.response.config.BranchConfigResponseDTO;
import com.iclass.video.entity.BranchConfig;
import com.iclass.video.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BranchConfigMapper {

    public void updateEntity(BranchConfig config, UpdateBranchConfigDTO dto, User updatedBy) {
        if (dto.getConfigValue() != null) {
            config.setConfigValue(dto.getConfigValue());
        }
        if (dto.getDescription() != null) {
            config.setDescription(dto.getDescription());
        }
        if (dto.getDisplayOrder() != null) {
            config.setDisplayOrder(dto.getDisplayOrder());
        }
        if (updatedBy != null) {
            config.setUpdatedBy(updatedBy);
        }
    }

    public BranchConfigResponseDTO toResponseDTO(BranchConfig config) {
        return BranchConfigResponseDTO.builder()
                .id(config.getId())
                .branchId(config.getBranch().getId())
                .branchName(config.getBranch().getName())
                .companyName(config.getBranch().getCompany().getName())
                .category(config.getCategory())
                .configKey(config.getConfigKey())
                .configValue(config.getConfigValue())
                .configType(config.getConfigType())
                .defaultValue(config.getDefaultValue())
                .description(config.getDescription())
                .validationRule(config.getValidationRule())
                .displayOrder(config.getDisplayOrder())
                .updatedByUsername(config.getUpdatedBy() != null ? config.getUpdatedBy().getUsername() : null)
                .updatedAt(config.getUpdatedAt())
                .build();
    }

    public List<BranchConfigResponseDTO> toResponseDTOList(List<BranchConfig> configs) {
        return configs.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }
}
