package com.iclass.video.service.impl;

import com.iclass.video.dto.request.branch.CreateBranchDTO;
import com.iclass.video.dto.request.branch.UpdateBranchDTO;
import com.iclass.video.dto.response.branch.BranchResponseDTO;
import com.iclass.video.entity.Branch;
import com.iclass.video.entity.Company;
import com.iclass.video.exception.DuplicateEntityException;
import com.iclass.video.exception.ResourceNotFoundException;
import com.iclass.video.mapper.BranchMapper;
import com.iclass.video.repository.BranchRepository;
import com.iclass.video.repository.CompanyRepository;
import com.iclass.video.service.BranchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BranchServiceImpl implements BranchService {
    private final BranchRepository branchRepository;
    private final BranchMapper branchMapper;
    private final CompanyRepository companyRepository;

    @Override
    @Transactional(readOnly = true)
    public List<BranchResponseDTO> findAll() {
        List<Branch> branches = branchRepository.findAll();
        return branchMapper.toResponseDTOList(branches);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BranchResponseDTO> findByCompanyId(Integer companyId) {
        List<Branch> branches = branchRepository.findByCompanyId(companyId);
        return branchMapper.toResponseDTOList(branches);
    }

    @Override
    @Transactional(readOnly = true)
    public BranchResponseDTO findById(Integer id) {
        Branch branch = branchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sucursal", id));

        return branchMapper.toResponseDTO(branch);
    }

    @Override
    @Transactional
    public BranchResponseDTO create(CreateBranchDTO dto) {
        if (branchRepository.existsByName(dto.getName())) {
            throw new DuplicateEntityException("Sucursal", "nombre", dto.getName());
        }

        Company company = companyRepository.findById(dto.getCompanyId())
                .orElseThrow(() -> new ResourceNotFoundException("Empresa", dto.getCompanyId()));

        Branch branch = branchMapper.toEntity(dto, company);
        Branch savedBranch = branchRepository.save(branch);

        return branchMapper.toResponseDTO(savedBranch);
    }

    @Override
    @Transactional
    public BranchResponseDTO update(Integer id, UpdateBranchDTO dto) {
        Branch branch = branchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sucursal", id));

        if (dto.getName() != null && !dto.getName().equals(branch.getName())) {
            if (branchRepository.existsByName(dto.getName())) {
                throw new DuplicateEntityException("Sucursal", "nombre", dto.getName());
            }
        }

        branchMapper.updateEntity(branch, dto);

        Branch updatedBranch = branchRepository.save(branch);

        return branchMapper.toResponseDTO(updatedBranch);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        if (!branchRepository.existsById(id)) {
            throw new ResourceNotFoundException("Sucursal", id);
        }

        branchRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void activate(Integer id) {
        Branch branch = branchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sucursal", id));

        branch.setIsActive(true);
        branchRepository.save(branch);
    }

    @Override
    @Transactional
    public void deactivate(Integer id) {
        Branch branch = branchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sucursal", id));

        branch.setIsActive(false);
        branchRepository.save(branch);
    }
}
