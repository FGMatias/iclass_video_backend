package com.iclassq.video.mapper;

import com.iclassq.video.dto.request.branch.CreateBranchDTO;
import com.iclassq.video.dto.request.branch.UpdateBranchDTO;
import com.iclassq.video.dto.response.branch.BranchResponseDTO;
import com.iclassq.video.entity.Branch;
import com.iclassq.video.entity.Company;
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

    public List<BranchResponseDTO> toResponseDTOList(List<Branch> branches) {
        return branches.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }
}
