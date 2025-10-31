package com.iclassq.video.mapper;

import com.iclassq.video.dto.request.company.CreateCompanyDTO;
import com.iclassq.video.dto.request.company.UpdateCompanyDTO;
import com.iclassq.video.dto.response.company.CompanyResponseDTO;
import com.iclassq.video.entity.Company;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CompanyMapper {
    private final UserMapper userMapper;

    public Company toEntity(CreateCompanyDTO dto) {
        return Company.builder()
                .name(dto.getName())
                .ruc(dto.getRuc())
                .direction(dto.getDirection())
                .phone(dto.getPhone())
                .email(dto.getEmail())
                .logo(dto.getLogo())
                .isActive(true)
                .build();
    }

    public void updateEntity(Company company, UpdateCompanyDTO dto) {
        if (dto.getName() != null) company.setName(dto.getName());
        if (dto.getRuc() != null) company.setRuc(dto.getRuc());
        if (dto.getDirection() != null) company.setDirection(dto.getDirection());
        if (dto.getPhone() != null) company.setPhone(dto.getPhone());
        if (dto.getEmail() != null) company.setEmail(dto.getEmail());
        if (dto.getLogo() != null) company.setLogo(dto.getLogo());
        if (dto.getIsActive() != null) company.setIsActive(dto.getIsActive());
    }

    public CompanyResponseDTO toResponseDTO(Company company) {
        return CompanyResponseDTO.builder()
                .id(company.getId())
                .name(company.getName())
                .ruc(company.getRuc())
                .direction(company.getDirection())
                .phone(company.getPhone())
                .email(company.getEmail())
                .logo(company.getLogo())
                .isActive(company.getIsActive())
                .createdAt(company.getCreatedAt())
                .updatedAt(company.getUpdatedAt())
                .build();
    }

    public List<CompanyResponseDTO> toResponseDTOList(List<Company> companies) {
        return companies.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }
}
