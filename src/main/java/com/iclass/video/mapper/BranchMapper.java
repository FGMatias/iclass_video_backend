package com.iclass.video.mapper;

import com.iclass.video.dto.request.branch.CreateBranchDTO;
import com.iclass.video.dto.request.branch.UpdateBranchDTO;
import com.iclass.video.dto.response.area.AreaResponseDTO;
import com.iclass.video.dto.response.branch.BranchDetailDTO;
import com.iclass.video.dto.response.branch.BranchResponseDTO;
import com.iclass.video.dto.response.user.UserResponseDTO;
import com.iclass.video.entity.Branch;
import com.iclass.video.entity.Company;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BranchMapper {

    public Branch toEntity(CreateBranchDTO dto, Company company) {
        return Branch.builder()
                .company(company)
                .name(dto.getName())
                .direction(dto.getDirection())
                .phone(dto.getPhone())
                .isActive(true)
                .build();
    }

    public void updateEntity(Branch branch, UpdateBranchDTO dto) {
        if (dto.getName() != null) branch.setName(dto.getName());
        if (dto.getDirection() != null) branch.setDirection(dto.getDirection());
        if (dto.getPhone() != null) branch.setPhone(dto.getPhone());
        if (dto.getIsActive() != null) branch.setIsActive(dto.getIsActive());
    }

    public BranchResponseDTO toResponseDTO(Branch branch) {
        return BranchResponseDTO.builder()
                .id(branch.getId())
                .companyId(branch.getCompany().getId())
                .companyName(branch.getCompany().getName())
                .name(branch.getName())
                .direction(branch.getDirection())
                .phone(branch.getPhone())
                .isActive(branch.getIsActive())
                .createdAt(branch.getCreatedAt())
                .updatedAt(branch.getUpdatedAt())
                .build();
    }

    public BranchDetailDTO toDetailDTO(
            Branch branch,
            List<UserResponseDTO> administrators,
            List<AreaResponseDTO> areas
    ) {
        return BranchDetailDTO.builder()
                .id(branch.getId())
                .companyId(branch.getCompany().getId())
                .companyName(branch.getCompany().getName())
                .name(branch.getName())
                .direction(branch.getDirection())
                .phone(branch.getPhone())
                .isActive(branch.getIsActive())
                .createdAt(branch.getCreatedAt())
                .updatedAt(branch.getUpdatedAt())
                .administrators(administrators)
                .areas(areas)
                .build();
    }

    public List<BranchResponseDTO> toResponseDTOList(List<Branch> branches) {
        return branches.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }
}
