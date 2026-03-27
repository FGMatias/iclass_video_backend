package com.iclass.video.service.impl;

import com.iclass.video.dto.request.area.CreateAreaDTO;
import com.iclass.video.dto.request.area.UpdateAreaDTO;
import com.iclass.video.dto.response.area.AreaDetailDTO;
import com.iclass.video.dto.response.area.AreaResponseDTO;
import com.iclass.video.entity.Area;
import com.iclass.video.entity.AreaVideo;
import com.iclass.video.entity.Branch;
import com.iclass.video.entity.DeviceArea;
import com.iclass.video.exception.DuplicateEntityException;
import com.iclass.video.exception.ResourceNotFoundException;
import com.iclass.video.mapper.AreaMapper;
import com.iclass.video.repository.AreaRepository;
import com.iclass.video.repository.AreaVideoRepository;
import com.iclass.video.repository.BranchRepository;
import com.iclass.video.repository.DeviceAreaRepository;
import com.iclass.video.service.AreaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AreaServiceImpl implements AreaService {
    private final AreaRepository areaRepository;
    private final BranchRepository branchRepository;
    private final AreaVideoRepository areaVideoRepository;
    private final DeviceAreaRepository deviceAreaRepository;
    private final AreaMapper areaMapper;

    @Override
    @Transactional(readOnly = true)
    public List<AreaResponseDTO> findAll() {
        List<Area> areas = areaRepository.findAll();
        return areaMapper.toResponseDTOList(areas);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AreaResponseDTO> findByBranchId(Integer branchId) {
        if (!branchRepository.existsById(branchId)) {
            throw new ResourceNotFoundException("Sucursal", branchId);
        }

        List<Area> areas = areaRepository.findByBranchId(branchId);
        return areaMapper.toResponseDTOList(areas);
    }

    @Override
    @Transactional(readOnly = true)
    public AreaResponseDTO findById(Integer id) {
        Area area = areaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Area", id));

        return areaMapper.toResponseDTO(area);
    }

    @Override
    @Transactional
    public AreaResponseDTO create(CreateAreaDTO dto) {
        if (areaRepository.existsByName(dto.getName())) {
            throw new DuplicateEntityException("Area", "nombre", dto.getName());
        }

        Branch branch = branchRepository.findById(dto.getBranchId())
                .orElseThrow(() -> new ResourceNotFoundException("Sucursal", dto.getBranchId()));

        Area area = areaMapper.toEntity(dto, branch);
        Area savedArea = areaRepository.save(area);

        return areaMapper.toResponseDTO(savedArea);
    }

    @Override
    @Transactional
    public AreaResponseDTO update(Integer id, UpdateAreaDTO dto) {
        Area area = areaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Area", id));

        if (dto.getName() != null && !dto.getName().equals(area.getName())) {
            if (areaRepository.existsByName(dto.getName())) {
                throw new DuplicateEntityException("Area", "nombre", id);
            }
        }

        areaMapper.updateEntity(area, dto);

        Area updatedArea = areaRepository.save(area);

        return areaMapper.toResponseDTO(updatedArea);
    }

    @Override
    @Transactional(readOnly = true)
    public AreaDetailDTO detail(Integer id) {
        Area area = areaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Area", id));

        List<AreaVideo> areaVideos = areaVideoRepository.findByAreaWithVideos(id);
        List<DeviceArea> currentDevices = deviceAreaRepository.findCurrentByAreaId(id);

        return areaMapper.toDetailDTO(area, areaVideos, currentDevices);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        if (!areaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Area", id);
        }

        areaRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void activate(Integer id) {
        Area area = areaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Area", id));

        area.setIsActive(true);
        areaRepository.save(area);
    }

    @Override
    @Transactional
    public void deactivate(Integer id) {
        Area area = areaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Area", id));

        area.setIsActive(false);
        areaRepository.save(area);
    }
}
