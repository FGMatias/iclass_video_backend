package com.iclass.video.service.impl;

import com.iclass.video.constants.RoleConstants;
import com.iclass.video.dto.request.company.CreateCompanyDTO;
import com.iclass.video.dto.request.company.UpdateCompanyDTO;
import com.iclass.video.dto.response.branch.BranchResponseDTO;
import com.iclass.video.dto.response.company.CompanyDetailDTO;
import com.iclass.video.dto.response.company.CompanyResponseDTO;
import com.iclass.video.dto.response.user.UserResponseDTO;
import com.iclass.video.dto.response.user.UserSimpleDTO;
import com.iclass.video.entity.Branch;
import com.iclass.video.entity.Company;
import com.iclass.video.entity.UserCompany;
import com.iclass.video.exception.DuplicateEntityException;
import com.iclass.video.exception.ResourceNotFoundException;
import com.iclass.video.mapper.BranchMapper;
import com.iclass.video.mapper.CompanyMapper;
import com.iclass.video.mapper.UserMapper;
import com.iclass.video.repository.BranchRepository;
import com.iclass.video.repository.CompanyRepository;
import com.iclass.video.repository.UserCompanyRepository;
import com.iclass.video.repository.VideoRepository;
import com.iclass.video.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {
    private final CompanyRepository companyRepository;
    private final CompanyMapper companyMapper;
    private final UserCompanyRepository userCompanyRepository;
    private final BranchRepository branchRepository;
    private final VideoRepository videoRepository;
    private final UserMapper userMapper;
    private final BranchMapper branchMapper;

    @Override
    @Transactional(readOnly = true)
    public List<CompanyResponseDTO> findAll() {
        List<Company> companies = companyRepository.findAll();
        return companyMapper.toResponseDTOList(companies);
    }

    @Override
    @Transactional(readOnly = true)
    public CompanyResponseDTO findById(Integer id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa", id));

        return companyMapper.toResponseDTO(company);
    }

    @Override
    @Transactional
    public CompanyResponseDTO create(CreateCompanyDTO dto) {
        if (companyRepository.existsByName(dto.getName())) {
            throw new DuplicateEntityException("Empresa", "nombre", dto.getName());
        }

        if (companyRepository.existsByRuc(dto.getRuc())) {
            throw new DuplicateEntityException("Empresa", "ruc", dto.getRuc());
        }

        Company company = companyMapper.toEntity(dto);
        Company savedCompany = companyRepository.save(company);

        return companyMapper.toResponseDTO(savedCompany);
    }

    @Override
    @Transactional
    public CompanyResponseDTO update(Integer id, UpdateCompanyDTO dto) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa", id));

        if (dto.getName() != null && !dto.getName().equals(company.getName())) {
            if (companyRepository.existsByName(dto.getName())) {
                throw new DuplicateEntityException("Empresa", "nombre", dto.getName());
            }
        }

        if (dto.getRuc() != null && !dto.getRuc().equals(company.getRuc())) {
            if (companyRepository.existsByRuc(dto.getRuc())) {
                throw new DuplicateEntityException("Empresa", "ruc", dto.getRuc());
            }
        }

        companyMapper.updateEntity(company, dto);

        Company updatedCompany = companyRepository.save(company);

        return companyMapper.toResponseDTO(updatedCompany);
    }

    @Override
    @Transactional(readOnly = true)
    public CompanyDetailDTO detail(Integer id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa", id));

        List<UserResponseDTO> administrators = userCompanyRepository.findByCompanyId(id)
                .stream()
                .map(UserCompany::getUser)
                .filter(user -> user.getRole().getId() == RoleConstants.ID_ADMINISTRADOR_EMPRESA && user.getIsActive())
                .map(userMapper::toResponseDTO)
                .toList();

        List<Branch> branches = branchRepository.findByCompanyId(id);
        List<BranchResponseDTO> branchDTOs = branchMapper.toResponseDTOList(branches);

        Integer totalVideos = videoRepository.countByCompanyId(id);

        return companyMapper.toDetailDTO(company, administrators, branchDTOs, totalVideos);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        if (!companyRepository.existsById(id)) {
            throw new ResourceNotFoundException("Empresa", id);
        }

        companyRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void activate(Integer id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa", id));

        company.setIsActive(true);
        companyRepository.save(company);
    }

    @Override
    @Transactional
    public void deactivate(Integer id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa", id));

        company.setIsActive(false);
        companyRepository.save(company);
    }
}
