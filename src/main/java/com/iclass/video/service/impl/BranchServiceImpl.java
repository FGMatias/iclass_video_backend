package com.iclass.video.service.impl;

import com.iclass.video.constants.RoleConstants;
import com.iclass.video.dto.request.branch.CreateBranchDTO;
import com.iclass.video.dto.request.branch.UpdateBranchDTO;
import com.iclass.video.dto.response.area.AreaResponseDTO;
import com.iclass.video.dto.response.branch.BranchDetailDTO;
import com.iclass.video.dto.response.branch.BranchResponseDTO;
import com.iclass.video.dto.response.device.DeviceInfo;
import com.iclass.video.dto.response.user.UserResponseDTO;
import com.iclass.video.entity.*;
import com.iclass.video.exception.DuplicateEntityException;
import com.iclass.video.exception.ResourceNotFoundException;
import com.iclass.video.mapper.AreaMapper;
import com.iclass.video.mapper.BranchMapper;
import com.iclass.video.mapper.DeviceMapper;
import com.iclass.video.mapper.UserMapper;
import com.iclass.video.repository.*;
import com.iclass.video.service.BranchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BranchServiceImpl implements BranchService {
    private final BranchRepository branchRepository;
    private final CompanyRepository companyRepository;
    private final UserBranchRepository userBranchRepository;
    private final AreaRepository areaRepository;
    private final DeviceRepository deviceRepository;
    private final BranchMapper branchMapper;
    private final UserMapper userMapper;
    private final AreaMapper areaMapper;
    private final DeviceMapper deviceMapper;

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
    @Transactional(readOnly = true)
    public BranchDetailDTO detail(Integer id) {
        Branch branch = branchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sucursal", id));

        List<UserResponseDTO> administrators = userBranchRepository.findByBranchId(id)
                .stream()
                .map(UserBranch::getUser)
                .filter(user -> user.getRole().getId() == RoleConstants.ID_ADMINISTRADOR_SUCURSAL)
                .map(userMapper::toResponseDTO)
                .toList();

        List<Area> areas = areaRepository.findByBranchId(id);
        List<AreaResponseDTO> areaDTOs = areaMapper.toResponseDTOList(areas);

        List<Device> devices = deviceRepository.findByBranchId(id);
        List<DeviceInfo> deviceInfos = deviceMapper.toDeviceInfoList(devices);

        return branchMapper.toDetailDTO(branch, administrators, areaDTOs, deviceInfos);
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
